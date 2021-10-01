package com.nmmoc7.polymercore.common.item;

import com.nmmoc7.polymercore.PolymerCore;
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
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PolymerCore.MOD_ID);
        public static final RegistryObject<Item> obsidianIngot = ITEMS.register("test_item", TestItem::new);
    }
}
