package com.nmmoc7.polymercore.common.capability.recipe;

import com.nmmoc7.polymercore.api.capability.IPolymerCapability;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemStackHandler;

public class ItemCapability extends ItemStackHandler implements IPolymerCapability {
    @Override
    public CompoundNBT serializeNBT() {
        return super.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
    }
}
