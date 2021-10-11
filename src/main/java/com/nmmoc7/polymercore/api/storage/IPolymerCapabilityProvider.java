package com.nmmoc7.polymercore.api.storage;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

public interface IPolymerCapabilityProvider extends INBTSerializable<CompoundNBT>, ICapabilityProvider {
}
