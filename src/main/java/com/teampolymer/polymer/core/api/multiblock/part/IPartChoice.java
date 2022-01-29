package com.teampolymer.polymer.core.api.multiblock.part;


import com.teampolymer.polymer.core.api.multiblock.MultiblockDirection;

public interface IPartChoice {

    IMultiblockUnit getUnit(MultiblockDirection appliedDirection);
    IMultiblockUnit getUnit();
    String getType();
    boolean canBeSample();
}
