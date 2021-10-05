package com.nmmoc7.polymercore.common.handler;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.api.multiblock.IMultiblockType;
import com.nmmoc7.polymercore.api.registry.PolymerCoreRegistries;
import com.nmmoc7.polymercore.common.multiblock.free.MultiblockTypeFree;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MultiblockRegisterHandler {
    public static final DeferredRegister<IMultiblockType> MULTIBLOCK_TYPES = DeferredRegister.create(PolymerCoreRegistries.MULTIBLOCK_TYPES, PolymerCoreApi.MOD_ID);


    public static final RegistryObject<IMultiblockType> TYPE_FREE = MULTIBLOCK_TYPES.register("type_free", MultiblockTypeFree::new);

}
