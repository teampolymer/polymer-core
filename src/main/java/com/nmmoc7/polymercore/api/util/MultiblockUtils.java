package com.nmmoc7.polymercore.api.util;

import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import com.nmmoc7.polymercore.api.multiblock.IMultiblockType;
import com.nmmoc7.polymercore.api.registry.PolymerCoreRegistries;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class MultiblockUtils {
    public static IAssembledMultiblock deserializeNBT(World world, INBT nbt) {
        if (!(nbt instanceof CompoundNBT)) {
            return null;
        }
        CompoundNBT compound = (CompoundNBT) nbt;
        String type = compound.getString("type");
        IMultiblockType typeObj = PolymerCoreRegistries.MULTIBLOCK_TYPES.getValue(new ResourceLocation(type));
        if (typeObj == null) {
            return null;
        }
        return typeObj.createFromNBT(world, compound);
    }
}
