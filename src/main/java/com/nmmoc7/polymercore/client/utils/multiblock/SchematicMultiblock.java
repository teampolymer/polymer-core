package com.nmmoc7.polymercore.client.utils.multiblock;

import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.MultiblockDirection;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
import com.nmmoc7.polymercore.api.multiblock.part.IPartChoice;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.vector.Vector3i;

import java.util.*;

/**
 * 用于投影的结构（TODO: 拓展（这是第几个拓展TODO了.jpg）
 */
public class SchematicMultiblock {
    private final IDefinedMultiblock origin;
    private Map<Vector3i, ISampleProvider> originSamples;
    private Map<Vector3i, ISampleProvider> flippedSamples;

    public SchematicMultiblock(IDefinedMultiblock origin) {
        this.origin = origin;
        this.originSamples = new HashMap<>();
        this.flippedSamples = new HashMap<>();
        for (Map.Entry<Vector3i, IMultiblockPart> entry : origin.getParts().entrySet()) {
            Vector3i key = entry.getKey();
            List<BlockState> originSamples = new ArrayList<>();
            List<BlockState> flippedSamples = new ArrayList<>();

            IMultiblockPart value = entry.getValue();
            Collection<IPartChoice> choices = value.sampleChoices();
            if (choices.isEmpty()) {
                IPartChoice choice = value.defaultChoice();
                if (choice == null) {
                    throw new IllegalArgumentException("The part choice has nether sample nor default choices");
                }
                originSamples.addAll(choice.getUnit().getSampleBlocks());
                flippedSamples.addAll(choice.getUnit().withDirection(MultiblockDirection.NONE_FLIPPED).getSampleBlocks());

            } else {
                for (IPartChoice choice : value.sampleChoices()) {
                    originSamples.addAll(choice.getUnit().getSampleBlocks());
                    flippedSamples.addAll(choice.getUnit().withDirection(MultiblockDirection.NONE_FLIPPED).getSampleBlocks());
                }
            }
            BlockState[] originSamplesArray = originSamples.toArray(new BlockState[0]);
            this.originSamples.put(key, iter -> originSamplesArray[iter % originSamplesArray.length]);
            BlockState[] flippedSamplesArray = flippedSamples.toArray(new BlockState[0]);
            this.flippedSamples.put(key, iter -> flippedSamplesArray[iter % flippedSamplesArray.length]);
        }

        this.originSamples = Collections.unmodifiableMap(originSamples);
        this.flippedSamples = Collections.unmodifiableMap(flippedSamples);
    }

    public IDefinedMultiblock getOriginalMultiblock() {
        return origin;
    }


    public Map<Vector3i, ISampleProvider> getSamples(boolean isFlipped) {
        return isFlipped ? flippedSamples : originSamples;
    }


}
