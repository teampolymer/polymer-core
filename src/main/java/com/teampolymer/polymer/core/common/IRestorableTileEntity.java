package com.teampolymer.polymer.core.common;

import net.minecraft.nbt.CompoundNBT;

public interface IRestorableTileEntity {
    void readRestorableFromNBT(CompoundNBT tagCompound);
    CompoundNBT writeRestorableToNBT(CompoundNBT compound);
}
