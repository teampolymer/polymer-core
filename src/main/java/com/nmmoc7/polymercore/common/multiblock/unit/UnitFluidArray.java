package com.nmmoc7.polymercore.common.multiblock.unit;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UnitFluidArray extends AbstractUnit {
    private final List<Fluid> fluids;

    public UnitFluidArray(List<Fluid> fluids) {
        super(null);
        this.fluids = ImmutableList.copyOf(fluids);

        this.samples = Collections.unmodifiableList(
            fluids.stream()
                .map(it -> it.defaultFluidState().createLegacyBlock())
                .collect(Collectors.toList()));
    }


    @Override
    public boolean test(BlockState block) {
        final Fluid condition = block.getFluidState().getType();
        return fluids.contains(condition);
    }
}
