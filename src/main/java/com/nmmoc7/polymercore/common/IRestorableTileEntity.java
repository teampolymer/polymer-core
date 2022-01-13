package com.nmmoc7.polymercore.common;

import net.minecraft.nbt.CompoundNBT;

public interface IRestorableTileEntity {
    void readRestorableFromNBT(CompoundNBT tagCompound);
    CompoundNBT writeRestorableToNBT(CompoundNBT compound);
}
