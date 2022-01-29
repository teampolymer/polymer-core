package com.teampolymer.polymer.core.common.capability.blueprint;

import com.teampolymer.polymer.core.api.capability.IMultiblockLocateHandler;
import com.teampolymer.polymer.core.api.capability.IMultiblockSupplier;
import com.teampolymer.polymer.core.common.capability.EmptyStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityMultiblockItem {
    @CapabilityInject(IMultiblockSupplier.class)
    public static Capability<IMultiblockSupplier> MULTIBLOCK_SUPPLIER = null;
    @CapabilityInject(IMultiblockLocateHandler.class)
    public static Capability<IMultiblockLocateHandler> MULTIBLOCK_LOCATE_HANDLER = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IMultiblockSupplier.class, EmptyStorage.getInstance(), () -> null);
        CapabilityManager.INSTANCE.register(IMultiblockLocateHandler.class, EmptyStorage.getInstance(), () -> null);
    }

}
