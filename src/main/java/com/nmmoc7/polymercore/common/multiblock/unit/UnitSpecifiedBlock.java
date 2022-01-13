package com.nmmoc7.polymercore.common.multiblock.unit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.util.Collections;

public class UnitSpecifiedBlock extends AbstractUnit {
    private final Block block;

    public UnitSpecifiedBlock(Block block) {
        super(null);
        this.block = block;
        this.samples = Collections.singletonList(block.defaultBlockState());
    }

    @Override
    public BlockState getSampleBlock() {
        return block.defaultBlockState();
    }

    @Override
    public boolean test(BlockState block) {
        return block.getBlock() == this.block;
    }
}
