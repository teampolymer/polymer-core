package com.nmmoc7.polymercore.common.item;

import net.minecraft.item.ItemGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Testitem extends Item {
    public Testitem() {
        super(new Properties().group(ItemGroup.MATERIALS));
    }

    public class ItemRegistry {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,Utils.MOD_ID)
        public static final RegistryObject<Item> obsidianIngot = ITEMS.register("test_item", ObsidianIngot::new);
    }
}
