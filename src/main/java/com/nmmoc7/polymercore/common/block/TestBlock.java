package com.nmmoc7.polymercore.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TestBlock extends Block {
    public TestBlock() {
        super(Properties.create(Material.ROCK).hardnessAndResistance(5));
    }

    public static class BlockRegistry {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PolymerCoreApi.MOD_ID);
        public static final RegistryObject<Block> TestBlock = BLOCKS.register("machine_base", TestBlock::new);
    }
}
