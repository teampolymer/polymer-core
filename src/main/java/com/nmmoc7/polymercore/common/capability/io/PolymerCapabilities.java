package com.nmmoc7.polymercore.common.capability.io;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class PolymerCapabilities {
    @CapabilityInject(SimpleItemCapability.class)
    public static Capability<SimpleItemCapability> ITEM;
}
