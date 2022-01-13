package com.nmmoc7.polymercore.common.multiblock.unit;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;

import java.util.Collections;

public class UnitFluidState extends AbstractUnit {
    private final FluidState fluidState;

    public UnitFluidState(FluidState fluid) {
        super(Collections.singletonList(fluid.createLegacyBlock()));
        this.fluidState = fluid;
    }

    @Override
    public boolean test(BlockState block) {
        return fluidState == block.getFluidState();
    }
}
