package com.nmmoc7.polymercore.common.capability.blueprint;

import com.nmmoc7.polymercore.api.capability.IMultiblockLocateHandler;
import com.nmmoc7.polymercore.api.capability.IMultiblockSupplier;
import com.nmmoc7.polymercore.common.capability.EmptyStorage;
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
