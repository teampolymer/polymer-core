package com.nmmoc7.polymercore.common;

import com.nmmoc7.polymercore.common.item.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class PolymerItemGroup extends ItemGroup {
    public static final PolymerItemGroup INSTANCE = new PolymerItemGroup();

    public PolymerItemGroup() {
        super("polymer");
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ModItems.BLUEPRINT.get());
    }
}
