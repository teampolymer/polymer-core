package com.teampolymer.polymer.core.common.capability;

import com.teampolymer.polymer.core.common.capability.io.SimpleItemCapability;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class PolymerCapabilities {
    @CapabilityInject(SimpleItemCapability.class)
    public static Capability<SimpleItemCapability> ITEM;
}
