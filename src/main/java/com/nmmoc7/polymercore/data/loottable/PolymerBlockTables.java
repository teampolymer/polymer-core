package com.nmmoc7.polymercore.data.loottable;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.common.block.ModBlocks;
import com.nmmoc7.polymercore.common.loot.functions.SaveRestorableNbt;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.registries.ForgeRegistries;

public class PolymerBlockTables extends BlockLootTables {
    @Override
    protected void addTables() {
        registerLootTable(ModBlocks.TestBlock.get(), block -> droppingWithRestorableNbt(block));
    }

    protected static LootTable.Builder droppingWithRestorableNbt(IItemProvider item) {
        LootPool.Builder pool = LootPool.builder()
            .rolls(ConstantRange.of(1))
            .addEntry(ItemLootEntry.builder(item)
                .acceptFunction(SaveRestorableNbt.builder()));

        return LootTable.builder()
            .addLootPool(pool);
    }

    protected static LootTable.Builder droppingWithCopyNbt(IItemProvider item) {
        LootPool.Builder pool = LootPool.builder()
            .rolls(ConstantRange.of(1))
            .addEntry(ItemLootEntry.builder(item)
                .acceptFunction(CopyNbt.builder(CopyNbt.Source.BLOCK_ENTITY)
                    .replaceOperation("energy", "energy")
                    .replaceOperation("items", "data.items")
                )
            );

        return LootTable.builder()
            .addLootPool(pool);
    }


    @Override
    protected Iterable<Block> getKnownBlocks() {
        return () -> ForgeRegistries.BLOCKS.getValues().stream().filter(it -> PolymerCoreApi.MOD_ID.equals(it.getRegistryName().getNamespace())).iterator();
    }
}
