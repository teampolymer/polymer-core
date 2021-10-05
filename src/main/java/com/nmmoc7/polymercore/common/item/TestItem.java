package com.nmmoc7.polymercore.common.item;

import com.nmmoc7.polymercore.common.ModGroup;
import com.nmmoc7.polymercore.common.block.TestBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TestItem extends Item {
    public TestItem() {
        super(new Properties().group(ItemGroup.MATERIALS));
    }

    public static class ItemRegistry {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PolymerCoreApi.MOD_ID);
        public static final RegistryObject<Item> TestItem = ITEMS.register("test_item", TestItem::new);
        public static final RegistryObject<Item> obsidianBlock = ITEMS.register("obsidian_block", () -> new BlockItem(TestBlock.BlockRegistry.TestBlock.get(), new Properties().group(ModGroup.polymergroup)));
    }
}
