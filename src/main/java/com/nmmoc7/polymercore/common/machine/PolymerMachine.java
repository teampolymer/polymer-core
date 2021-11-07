package com.nmmoc7.polymercore.common.machine;

import com.nmmoc7.polymercore.api.machine.IMachine;
import com.nmmoc7.polymercore.api.storage.IPolymerCapabilityProvider;
import com.nmmoc7.polymercore.common.capability.PolymerCapabilities;
import com.nmmoc7.polymercore.common.capability.io.PolymerCapabilityProvider;
import com.nmmoc7.polymercore.common.capability.io.SimpleItemCapability;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PolymerMachine implements IMachine {
    final IPolymerCapabilityProvider capabilityProvider = new PolymerCapabilityProvider()
            .add(new ResourceLocation("item"), PolymerCapabilities.ITEM, LazyOptional.of(SimpleItemCapability::new));

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return capabilityProvider.getCapability(cap, side);
    }
}
