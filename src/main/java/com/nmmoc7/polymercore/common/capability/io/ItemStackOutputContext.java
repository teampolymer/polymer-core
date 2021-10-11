package com.nmmoc7.polymercore.common.capability.io;

import com.nmmoc7.polymercore.api.storage.io.IOutputContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ItemStackOutputContext implements IOutputContext<ItemStack> {
    Map<Item, Integer> items = new HashMap<>();

    public ItemStackOutputContext(ItemStack... itemStacks) {
        for (ItemStack itemStack : itemStacks) {
            if (items.get(itemStack.getItem()) == null) {
                items.put(itemStack.getItem(), itemStack.getCount());
            }
            else {
                items.put(itemStack.getItem(), items.get(itemStack.getItem()) + itemStack.getCount());
            }
        }
    }

    Map<Item, Integer> get() {
        return items;
    }
}
