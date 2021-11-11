package com.nmmoc7.polymercore.common.multiblock.part;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UnitFluidStateArray extends AbstractUnit {
    private final List<FluidState> stateList;

    public UnitFluidStateArray(List<FluidState> states) {
        super(null);
        stateList = ImmutableList.copyOf(states);
        samples = Collections.unmodifiableList(stateList.stream().map(FluidState::getBlockState).collect(Collectors.toList()));
    }


    @Override
    public boolean test(BlockState block) {
        return stateList.contains(block.getFluidState());
    }
}
