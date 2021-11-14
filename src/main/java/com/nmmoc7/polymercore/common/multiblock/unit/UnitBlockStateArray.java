package com.nmmoc7.polymercore.common.multiblock.unit;

import com.google.common.collect.ImmutableList;
import com.nmmoc7.polymercore.api.multiblock.MultiblockDirection;
import com.nmmoc7.polymercore.api.multiblock.part.IDirectionUnit;
import com.nmmoc7.polymercore.api.util.MultiblockPartUtils;
import net.minecraft.block.BlockState;

import java.util.List;
import java.util.stream.Collectors;

public class UnitBlockStateArray extends AbstractDirectionUnit {
    private final List<BlockState> stateList;

    public UnitBlockStateArray(List<BlockState> states) {
        this(states, MultiblockDirection.NONE);
    }

    public UnitBlockStateArray(List<BlockState> states, MultiblockDirection direction) {
        super(null, direction);
        stateList = ImmutableList.copyOf(states);
        samples = stateList;
    }

    public UnitBlockStateArray(List<BlockState> states, List<BlockState> samples, MultiblockDirection direction) {
        super(samples, direction);
        stateList = ImmutableList.copyOf(states);
    }

    @Override
    public boolean test(BlockState block) {
        return stateList.contains(block);
    }

    @Override
    public IDirectionUnit withDirection(MultiblockDirection direction) {
        if (direction == this.getDirection()) {
            return this;
        }
        List<BlockState> blockStates = this.stateList.stream()
            .map(it -> MultiblockPartUtils.withDirection(it, direction))
            .collect(Collectors.toList());
        return new UnitBlockStateArray(blockStates, direction);
    }
}
