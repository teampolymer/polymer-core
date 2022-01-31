package com.teampolymer.polymer.core.common.block;

import com.teampolymer.polymer.core.api.PolymerCoreApi;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, PolymerCoreApi.MOD_ID);
}
