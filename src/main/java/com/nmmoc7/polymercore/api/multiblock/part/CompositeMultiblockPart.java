package com.nmmoc7.polymercore.api.multiblock.part;

import net.minecraft.block.BlockState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * 组合两个IMultiblockPart
 */
public class CompositeMultiblockPart implements IMultiblockPart {
    public CompositeMultiblockPart(IMultiblockPart first, IMultiblockPart second) {
        this.first = first;
        this.second = second;
    }

    private final IMultiblockPart first;
    private final IMultiblockPart second;

    private Collection<BlockState> samples;

    @Override
    public BlockState getFirstMatchingBlock() {
        return first.getFirstMatchingBlock();
    }

    @Override
    public Collection<BlockState> getSampleBlocks() {
        if (samples == null) {
            Collection<BlockState> sample1 = first.getSampleBlocks();
            Collection<BlockState> sample2 = second.getSampleBlocks();
            ArrayList<BlockState> result = new ArrayList<>(sample1.size() + sample2.size());
            result.addAll(sample1);
            result.addAll(sample2);
            samples = result;
        }
        return samples;
    }

    @Override
    public boolean test(BlockState block) {
        return first.test(block) || second.test(block);
    }
}
