package com.nmmoc7.polymercore.common.multiblock.part;

import com.google.common.collect.ImmutableList;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import net.minecraft.block.BlockState;

import java.util.List;

public class PartBlockStateArray implements IMultiblockPart {
    private final List<BlockState> stateList;

    public PartBlockStateArray(List<BlockState> states) {
        stateList = ImmutableList.copyOf(states);
    }

    @Override
    public BlockState getFirstMatchingBlock() {
        return stateList.get(0);
    }

    @Override
    public List<BlockState> getSampleBlocks() {
        return stateList;
    }

    @Override
    public boolean test(BlockState block) {
        return stateList.contains(block);
    }
}
