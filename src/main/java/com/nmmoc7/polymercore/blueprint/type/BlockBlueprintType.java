package com.nmmoc7.polymercore.blueprint.type;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockBlueprintType implements IBlueprintType {
    final Block block;

    public BlockBlueprintType(ResourceLocation regName) {
        this.block = ForgeRegistries.BLOCKS.getValue(regName);
    }

    public BlockBlueprintType(BlockItem blockItem) {
        this.block = blockItem.getBlock();
    }

    public BlockBlueprintType(Block block) {
        this.block = block;
    }

    @Override
    public boolean test(BlockState block) {
        return this.block == block.getBlock();
    }
}
