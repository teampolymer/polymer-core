package com.nmmoc7.polymercore.common.multiblock.unit;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;

import java.util.Collections;

public class UnitSpecifiedFluid extends AbstractUnit {
    private final Fluid fluid;

    public UnitSpecifiedFluid(Fluid fluid) {
        super(null);
        this.fluid = fluid;
        this.samples = Collections.singletonList(fluid.defaultFluidState().createLegacyBlock());
    }

    @Override
    public boolean test(BlockState block) {
        return block.getFluidState().getType() == this.fluid;
    }
}
