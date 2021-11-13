package com.nmmoc7.polymercore.api.util;

import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.assembled.IMultiblockAssembleRule;
import com.nmmoc7.polymercore.api.multiblock.builder.IPartBuilder;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
import com.nmmoc7.polymercore.api.multiblock.part.IPartChoice;
import com.nmmoc7.polymercore.api.multiblock.part.IPartLimitConfig;
import com.nmmoc7.polymercore.common.handler.MultiblockRegisterHandler;
import com.nmmoc7.polymercore.common.multiblock.assembled.AbstractAssembleRule;
import com.nmmoc7.polymercore.common.multiblock.builder.DefaultCharMarkedMultiblockBuilder;
import com.nmmoc7.polymercore.common.multiblock.builder.DefaultPartBuilder;
import com.nmmoc7.polymercore.common.multiblock.free.FreeMultiblockAssembleRule;
import com.nmmoc7.polymercore.common.multiblock.free.MultiblockTypeFree;
import com.nmmoc7.polymercore.common.multiblock.part.AbstractUnit;
import com.nmmoc7.polymercore.common.multiblock.part.UnitSpecifiedBlock;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MultiblockPartUtilsTest {

    private static final Logger LOG = LogManager.getLogger();

    private class UnitFake extends AbstractUnit {
        private String name;

        public UnitFake(String name) {
            super(Collections.emptyList());
            this.name = name;
        }

        @Override
        public boolean test(BlockState block) {
            return false;
        }

        @Override
        public String toString() {
            return "UnitFake{" +
                "name='" + name + '\'' +
                '}';
        }
    }

    @Test
    void iterateSamples() {
        Map<Vector3i, IMultiblockPart> partMap = new HashMap<>();
        List<IPartLimitConfig> partLimitConfigs = new ArrayList<>();
        partMap.put(new Vector3i(0, 0, 0), new DefaultPartBuilder()
            .setDefaultPart(new UnitFake("stone"))
            .addChoice("out", new UnitFake("gold"))
            .build());

        partMap.put(new Vector3i(0, 1, 0), new DefaultPartBuilder()
            .setDefaultPart(new UnitFake("stone"))
            .addChoice("out", new UnitFake("gold"))
            .addChoice("in", new UnitFake("lapis"))
            .build());
        partMap.put(new Vector3i(0, 2, 0), new DefaultPartBuilder()
            .setDefaultPart(new UnitFake("stone"))
            .addChoice("out", new UnitFake("gold"))
            .build());
        partMap.put(new Vector3i(0, -1, 0), new DefaultPartBuilder()
            .setDefaultPart(new UnitFake("stone"))
            .addChoice("out", new UnitFake("gold"))
            .build());

        Random random = new Random();
        for (int i = 100; i < 200; i++) {
            IPartBuilder builder = new DefaultPartBuilder()
                .setDefaultPart(new UnitFake("stone"));
            if (random.nextFloat() > 0.6) {
                builder.addChoice("out", new UnitFake("gold"));
            }
            if (random.nextFloat() > 0.6) {
                builder.addChoice("in", new UnitFake("gold"));
            }
            if (random.nextFloat() > 0.9) {
                builder.addChoice("energy", new UnitFake("energyPort"));
            }
            IMultiblockPart part = builder.build();
            partMap.put(new Vector3i(i, -1, 0), part);
        }
        for (int i = 100; i < 200; i++) {
            IPartBuilder builder = new DefaultPartBuilder()
                .setDefaultPart(new UnitFake("stone"));
            if (random.nextFloat() > 0.5) {
                builder.addChoice("energy", new UnitFake("energyPort"));
            }
            if (random.nextFloat() > 0.7) {
                builder.addChoice("mana", new UnitFake("mana"));
            }
            if (random.nextFloat() > 0.7) {
                builder.addChoice("monitor", new UnitFake("monitor"));
            }
            IMultiblockPart part = builder.build();
            partMap.put(new Vector3i(-5, -1, i), part);
        }

        partLimitConfigs.add(new IPartLimitConfig() {
            @Override
            public String getTargetType() {
                return "out";
            }

            @Override
            public int maxCount() {
                return 10;
            }

            @Override
            public int minCount() {
                return 1;
            }
        });
        partLimitConfigs.add(new IPartLimitConfig() {
            @Override
            public String getTargetType() {
                return "in";
            }

            @Override
            public int maxCount() {
                return -1;
            }

            @Override
            public int minCount() {
                return 1;
            }
        });


        partLimitConfigs.add(new IPartLimitConfig() {
            @Override
            public String getTargetType() {
                return "energy";
            }

            @Override
            public int maxCount() {
                return 20;
            }

            @Override
            public int minCount() {
                return 4;
            }
        });
        partLimitConfigs.add(new IPartLimitConfig() {
            @Override
            public String getTargetType() {
                return "monitor";
            }

            @Override
            public int maxCount() {
                return 1;
            }

            @Override
            public int minCount() {
                return 1;
            }
        });
        partLimitConfigs.add(new IPartLimitConfig() {
            @Override
            public String getTargetType() {
                return "mana";
            }

            @Override
            public int maxCount() {
                return 4;
            }

            @Override
            public int minCount() {
                return 4;
            }
        });
        IMultiblockAssembleRule rule = new IMultiblockAssembleRule() {

            @Override
            public CompoundNBT serializeNBT() {
                return null;
            }

            @Override
            public void deserializeNBT(CompoundNBT nbt) {

            }

            @Override
            public BlockPos getOffset() {
                return null;
            }

            @Override
            public boolean isSymmetrical() {
                return false;
            }

            @Override
            public Rotation getRotation() {
                return null;
            }

            @Override
            public Map<BlockPos, IMultiblockUnit> mapParts(IDefinedMultiblock originalMultiblock) {
                return null;
            }

            @Override
            public void makeChoice(Vector3i key, IPartChoice choice) {

            }

            @Override
            public String getChoiceType(Vector3i relativePos) {
                if (relativePos.equals(new Vector3i(0, 1, 0))) {
                    return "out";
                }
                return null;
            }
        };

        long time = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            MultiblockPartUtils.iter = i;
            MultiblockPartUtils.iterateSamples(partLimitConfigs, partMap, rule);
        }
        long elp = System.currentTimeMillis() - time;
        LOG.warn(elp);
    }
}