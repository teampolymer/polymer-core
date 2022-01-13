package com.nmmoc7.polymercore.common.item;

import com.nmmoc7.polymercore.api.item.IHammer;
import com.nmmoc7.polymercore.common.PolymerItemGroup;
import net.minecraft.item.Item;

public class Hammer extends Item implements IHammer {
    public Hammer() {
        super(new Item.Properties().tab(PolymerItemGroup.INSTANCE));
    }
}
