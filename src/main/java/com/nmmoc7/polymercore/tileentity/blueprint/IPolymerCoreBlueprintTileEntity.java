package com.nmmoc7.polymercore.tileentity.blueprint;

import com.nmmoc7.polymercore.blueprint.IBlueprint;
import net.minecraft.world.World;

public interface IPolymerCoreBlueprintTileEntity {
    IBlueprint getBlueprint();
    boolean test();
}
