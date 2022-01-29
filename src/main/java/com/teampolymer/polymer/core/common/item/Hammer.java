package com.teampolymer.polymer.core.common.item;

import com.teampolymer.polymer.core.api.item.IHammer;
import com.teampolymer.polymer.core.common.PolymerItemGroup;
import net.minecraft.item.Item;

public class Hammer extends Item implements IHammer {
    public Hammer() {
        super(new Item.Properties().tab(PolymerItemGroup.INSTANCE));
    }
}
