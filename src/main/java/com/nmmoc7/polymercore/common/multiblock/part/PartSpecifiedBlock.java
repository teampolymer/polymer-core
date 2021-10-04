package com.nmmoc7.polymercore.common.multiblock.part;

import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.util.Collection;
import java.util.List;

public class PartSpecifiedBlock implements IMultiblockPart {
    private final Block block;

    public PartSpecifiedBlock(Block block) {
        this.block = block;
    }

    @Override
    public BlockState getFirstMatchingBlock() {
        return block.getDefaultState();
    }

    @Override
    public List<BlockState> getSampleBlocks() {
        return block.getStateContainer().getValidStates();
    }

    @Override
    public boolean test(BlockState block) {
        return block.getBlock() == this.block;
    }
}
