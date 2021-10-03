package com.nmmoc7.polymercore.api.util;

import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import com.nmmoc7.polymercore.api.multiblock.IMultiblockType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public class MultiblockUtils {
    public static IAssembledMultiblock deserializeNBT(INBT nbt) {
        if (!(nbt instanceof CompoundNBT)) {
            return null;
        }
        CompoundNBT compound = (CompoundNBT) nbt;
        String type = compound.getString("type");
        //TODO 从TypeRegistry获取Type
        IMultiblockType typeObj = null;
        if (typeObj == null) {
            return null;
        }
        return typeObj.createFromNBT(compound);
    }
}
