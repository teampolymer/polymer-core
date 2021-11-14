package com.nmmoc7.polymercore.common.multiblock.part;

import com.nmmoc7.polymercore.api.multiblock.MultiblockDirection;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
import com.nmmoc7.polymercore.api.multiblock.part.IPartChoice;

import java.util.EnumMap;

public class DefaultPartChoice implements IPartChoice {
    private final String type;
    private final IMultiblockUnit unit;
    private final boolean sample;

    public DefaultPartChoice(String type, IMultiblockUnit unit) {
        this.type = type;
        this.unit = unit;
        this.sample = type != null;
    }

    public DefaultPartChoice(String type, IMultiblockUnit unit, boolean canBeSample) {
        this.type = type;
        this.unit = unit;
        this.sample = canBeSample;
    }

    @Override
    public IMultiblockUnit getUnit(MultiblockDirection appliedDirection) {
        return unit.withDirection(appliedDirection);
    }

    @Override
    public IMultiblockUnit getUnit() {
        return unit;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean canBeSample() {
        return sample;
    }
}
