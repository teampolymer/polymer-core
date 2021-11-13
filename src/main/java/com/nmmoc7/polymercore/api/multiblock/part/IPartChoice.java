package com.nmmoc7.polymercore.api.multiblock.part;


public interface IPartChoice {

    IMultiblockUnit getUnit();
    String getType();
    boolean canBeSample();
}
