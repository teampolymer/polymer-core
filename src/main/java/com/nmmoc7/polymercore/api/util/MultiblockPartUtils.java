package com.nmmoc7.polymercore.api.util;

import com.nmmoc7.polymercore.PolymerCore;
import com.nmmoc7.polymercore.api.multiblock.MultiblockDirection;
import com.nmmoc7.polymercore.api.multiblock.assembled.IMultiblockAssembleRule;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.multiblock.part.IPartChoice;
import com.nmmoc7.polymercore.api.multiblock.part.IPartLimitConfig;
import com.nmmoc7.polymercore.api.util.math.ExpandedHopcroftKarpBipartiteMatching;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.util.Mirror;
import net.minecraft.util.math.vector.Vector3i;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class MultiblockPartUtils {
    private static final Logger LOG = LogManager.getLogger();
    
    public static BlockState withDirection(BlockState origin, MultiblockDirection direction) {
        switch (direction) {
            case NONE:
            case CLOCKWISE_90:
            case CLOCKWISE_180:
            case COUNTERCLOCKWISE_90:
            default:
                return origin
                    .rotate(direction.getRotation());
            case NONE_FLIPPED:
            case CLOCKWISE_180_FLIPPED:
                return origin
                    .rotate(direction.getRotation())
                    .mirror(Mirror.FRONT_BACK);
            case CLOCKWISE_90_FLIPPED:
            case COUNTERCLOCKWISE_90_FLIPPED:
                return origin
                    .rotate(direction.getRotation())
                    .mirror(Mirror.LEFT_RIGHT);
        }
    }

    private static class PartTypeInfo {
        //样品数量
        public int sample = 0;
        //最小
        public int min = -1;
        //最大
        public int max = -1;
        //非样品数量
        public int current = 0;
        //投影已经放置的数量
        public int settled = 0;
        //被占用的位置数量
        public int usedSample = 0;
    }

    public static int iter = 10;

    @Deprecated
    public static ExpandedHopcroftKarpBipartiteMatching.Result iterateSamples(Collection<IPartLimitConfig> limitConfigs, Map<Vector3i, IMultiblockPart> partMap, IMultiblockAssembleRule assembleRule) {
        Object2IntMap<String> positions = new Object2IntOpenHashMap<>();
        Object2IntMap<String> parts = new Object2IntOpenHashMap<>();
        Map<String, Set<String>> possibleChoices = new HashMap<>();
        Map<String, Set<String>> blockedChoices = new HashMap<>();
        Set<String> dynamicPartTypes = new HashSet<>();
        Map<String, PartTypeInfo> partInfos = new HashMap<>();
        for (IPartLimitConfig limitConfig : limitConfigs) {
            PartTypeInfo info = partInfos.computeIfAbsent(limitConfig.getTargetType(), it -> new PartTypeInfo());
            info.min = limitConfig.minCount();
            info.max = limitConfig.maxCount();
        }

        //1.获取所有基本结构
        //2.计算出固定的组件数量，确定新的限制范围
        for (Map.Entry<Vector3i, IMultiblockPart> partEntry : partMap.entrySet()) {
            Collection<IPartChoice> samples = partEntry.getValue().sampleChoices();
            //不变的部分
            if (samples.size() <= 0) {
                IPartChoice defaultChoice = partEntry.getValue().defaultChoice();
                if (defaultChoice == null) {
                    throw new IllegalArgumentException("The part choice has nether sample nor default choices");
                }
                PartTypeInfo info = partInfos.computeIfAbsent(defaultChoice.getType(), it -> new PartTypeInfo());
                info.current++;
                continue;
            }
            //可选但是已经放置过方块的部分
            PartTypeInfo placedBlockInfo = null;
            if (assembleRule != null) {
                String settledChoice = assembleRule.getChoiceType(partEntry.getKey());
                if (settledChoice != null) {
                    placedBlockInfo = partInfos.computeIfAbsent(settledChoice, it -> new PartTypeInfo());
                    placedBlockInfo.settled++;
                }
            }
            //可选的其他部分
            String coordinates = partEntry.getKey().toShortString();
            positions.put(coordinates, 1);
            Set<String> types = new HashSet<>();

            if (placedBlockInfo == null) {
                for (IPartChoice sample : samples) {
                    PartTypeInfo info = partInfos.computeIfAbsent(sample.getType(), it -> new PartTypeInfo());
                    info.sample++;
                    dynamicPartTypes.add(sample.getType());
                    types.add(sample.getType());
                }
                possibleChoices.put(coordinates, types);
            } else {
                for (IPartChoice sample : samples) {
                    PartTypeInfo info = partInfos.computeIfAbsent(sample.getType(), it -> new PartTypeInfo());
                    info.sample++;
                    dynamicPartTypes.add(sample.getType());
                    types.add(sample.getType());
                }
                blockedChoices.put(coordinates, types);
            }

        }
        //3.根据迭代指针固定组件数量
        for (Map.Entry<String, PartTypeInfo> entry : partInfos.entrySet()) {
            PartTypeInfo info = entry.getValue();
            String type = entry.getKey();
            if (info.max >= 0 && info.max < info.current) {
                LOG.error("The current block count part type '{}''s size is more than provided multiblock max count, this may be incorrect！", type);
            }
            if (info.min > (info.current + info.sample)) {
                LOG.error("The available sample of part type '{}''s size is less than provided multiblock min count, this may be incorrect！", type);
            }
            if (!dynamicPartTypes.contains(type)) {

                continue;
            }

            int min = Math.max(info.min - info.current - info.settled, 0);
            int max;
            if (info.max < 0) {
                max = info.sample;
            } else {
                max = Math.max(info.max - info.current - info.settled, 0);
            }
            //如果组件的最大值已经是0，则代表组件已经不能呗选择，跳过
            if (max == 0) {
                continue;
            }

            int result = (min + iter) % (max + 1);
            if (result == 0) {
                continue;
            }
            parts.put(type, result);
        }

        //4.构造二分图
        ExpandedHopcroftKarpBipartiteMatching bipartiteMatching = new ExpandedHopcroftKarpBipartiteMatching(positions, parts, possibleChoices);
        //6.搜索匹配
        ExpandedHopcroftKarpBipartiteMatching.Result matching = bipartiteMatching.getMatching();

        Object2IntMap<String> restPositions = new Object2IntOpenHashMap<>();
        Object2IntMap<String> restParts = new Object2IntOpenHashMap<>();
        for (Object2IntMap.Entry<String> stringEntry : matching.unmatched.object2IntEntrySet()) {
            if (dynamicPartTypes.contains(stringEntry.getKey())) {
                restParts.put(stringEntry.getKey(), stringEntry.getIntValue());
            } else {
                restPositions.put(stringEntry.getKey(), stringEntry.getIntValue());
            }
        }
        if (!restParts.isEmpty()) {
//            ExpandedHopcroftKarpBipartiteMatching matchingRest = new ExpandedHopcroftKarpBipartiteMatching(restPositions, restParts, blockedChoices);
//            ExpandedHopcroftKarpBipartiteMatching.Result restMatching = matchingRest.getMatching();
//            restParts.clear();
//            for (Map.Entry<String, Object2IntMap<String>> entry : restMatching.matches.entrySet()) {
//                if (dynamicPartTypes.contains(entry.getKey())) {
//                }
//            }
        } else {
            return null;
        }

        return matching;
    }
}
