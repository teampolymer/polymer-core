package com.nmmoc7.polymercore.common.capability.io;

import com.nmmoc7.polymercore.api.storage.IPolymerCapabilityProvider;
import com.nmmoc7.polymercore.api.storage.IPolymerStorageCapability;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PolymerCapabilityProvider implements IPolymerCapabilityProvider {
    public final Map<Capability<? extends IPolymerStorageCapability<?, ?, ?, ?>>, LazyOptional<? extends IPolymerStorageCapability<?, ?, ?, ?>>> CAPABILITY_MAP = new HashMap<>();
    public final Map<ResourceLocation, LazyOptional<? extends IPolymerStorageCapability<?, ?, ?, ?>>> CAPABILITIES = new HashMap<>();

    public <T extends IPolymerStorageCapability<?, ?, ?, ?>> PolymerCapabilityProvider add(ResourceLocation name, Capability<T> capability, LazyOptional<T> lazyOptional) {
        CAPABILITIES.put(name, lazyOptional);
        CAPABILITY_MAP.put(capability, lazyOptional);
        return this;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return CAPABILITY_MAP.get(cap) == null ? LazyOptional.empty() : CAPABILITY_MAP.get(cap).cast();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT result = new CompoundNBT();
        CAPABILITIES.forEach((key, value) -> {
            result.put(key.toString(), value.resolve().get().serializeNBT());
        });
        return result;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        CAPABILITIES.forEach((key, value) -> {
            value.resolve().get().deserializeNBT((CompoundNBT) nbt.get(key.toString()));
        });
    }
}
