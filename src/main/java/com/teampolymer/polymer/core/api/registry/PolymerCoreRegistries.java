package com.teampolymer.polymer.core.api.registry;

import com.teampolymer.polymer.core.api.PolymerCoreApi;
import com.teampolymer.polymer.core.api.multiblock.IMultiblockType;
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
