package com.nmmoc7.polymercore.common.item;

import com.nmmoc7.polymercore.api.blueprint.IBlueprint;
import com.nmmoc7.polymercore.api.item.IBlueprintItem;
import com.nmmoc7.polymercore.common.PolymerItemGroup;
import com.nmmoc7.polymercore.common.blueprint.BlueprintRegisterHandler;
import net.minecraft.item.Item;

public class TestBlueprintItem extends Item implements IBlueprintItem {
    public TestBlueprintItem() {
        super(new Properties().group(PolymerItemGroup.INSTANCE));
    }

    @Override
    public IBlueprint getBlueprint() {
        return BlueprintRegisterHandler.TEST_BLUEPRINT.get();
    }
}
