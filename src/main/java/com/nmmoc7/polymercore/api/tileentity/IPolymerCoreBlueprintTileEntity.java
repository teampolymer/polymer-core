package com.nmmoc7.polymercore.api.tileentity;

import com.nmmoc7.polymercore.api.blueprint.IBlueprint;

public interface IPolymerCoreBlueprintTileEntity {
    IBlueprint getBlueprint();
    void setBlueprint(IBlueprint blueprint);
    boolean test();
}
