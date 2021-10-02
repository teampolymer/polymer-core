package com.nmmoc7.polymercore.core.event;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.eventbus.api.Event;

public class BlockStateChangeEvent extends Event {
    private final BlockPos pos;
    private final BlockState blockStateIn;
    private final BlockState newState;

    public BlockStateChangeEvent(BlockPos pos, BlockState blockStateIn, BlockState newState) {
        this.pos = pos;
        this.blockStateIn = blockStateIn;
        this.newState = newState;
    }

    public BlockPos getPos() {
        return pos;
    }

    public BlockState getBlockStateIn() {
        return blockStateIn;
    }

    public BlockState getNewState() {
        return newState;
    }
}
