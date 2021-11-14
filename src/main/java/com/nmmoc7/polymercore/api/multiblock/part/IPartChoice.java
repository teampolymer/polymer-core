package com.nmmoc7.polymercore.api.multiblock.part;


import com.nmmoc7.polymercore.api.multiblock.MultiblockDirection;

public interface IPartChoice {

    IMultiblockUnit getUnit(MultiblockDirection appliedDirection);
    IMultiblockUnit getUnit();
    String getType();
    boolean canBeSample();
}
