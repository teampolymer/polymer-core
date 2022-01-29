package com.teampolymer.polymer.core.data.loottable;

import com.teampolymer.polymer.core.api.PolymerCoreApi;
import com.teampolymer.polymer.core.common.block.ModBlocks;
import com.teampolymer.polymer.core.common.loot.functions.SaveRestorableNbt;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.registries.ForgeRegistries;

public class PolymerBlockTables extends BlockLootTables {
    @Override
    protected void addTables() {
        add(ModBlocks.TestBlock.get(), block -> droppingWithRestorableNbt(block));
    }

    protected static LootTable.Builder droppingWithRestorableNbt(IItemProvider item) {
        LootPool.Builder pool = LootPool.lootPool()
            .setRolls(ConstantRange.exactly(1))
            .add(ItemLootEntry.lootTableItem(item)
                .apply(SaveRestorableNbt.builder()));

        return LootTable.lootTable()
            .withPool(pool);
    }

    protected static LootTable.Builder droppingWithCopyNbt(IItemProvider item) {
        LootPool.Builder pool = LootPool.lootPool()
            .setRolls(ConstantRange.exactly(1))
            .add(ItemLootEntry.lootTableItem(item)
                .apply(CopyNbt.copyData(CopyNbt.Source.BLOCK_ENTITY)
                    .copy("energy", "energy")
                    .copy("items", "data.items")
                )
            );

        return LootTable.lootTable()
            .withPool(pool);
    }


    @Override
    protected Iterable<Block> getKnownBlocks() {
        return () -> ForgeRegistries.BLOCKS.getValues().stream().filter(it -> PolymerCoreApi.MOD_ID.equals(it.getRegistryName().getNamespace())).iterator();
    }
}
