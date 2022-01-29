package com.teampolymer.polymer.core.common.machine;

import com.teampolymer.polymer.core.api.machine.IMachine;
import com.teampolymer.polymer.core.api.storage.IPolymerCapabilityProvider;
import com.teampolymer.polymer.core.common.capability.PolymerCapabilities;
import com.teampolymer.polymer.core.common.capability.io.PolymerCapabilityProvider;
import com.teampolymer.polymer.core.common.capability.io.SimpleItemCapability;
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
