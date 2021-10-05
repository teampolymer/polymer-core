package com.nmmoc7.polymercore.api.registry;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.api.multiblock.IMultiblockType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class PolymerCoreRegistries {
    public static IForgeRegistry<IMultiblockType> MULTIBLOCK_TYPES =
        new RegistryBuilder<IMultiblockType>()
            .setName(new ResourceLocation(PolymerCoreApi.MOD_ID, "multiblock_type"))
            .setType(IMultiblockType.class)
            .disableSaving()
            .create();

}
