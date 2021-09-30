package com.nmmoc7.polymercore.common.tileentity.blueprint;

import com.nmmoc7.polymercore.api.blueprint.IBlueprint;
import com.nmmoc7.polymercore.api.blueprint.type.IBlueprintType;
import com.nmmoc7.polymercore.api.tileentity.IPolymerCoreBlueprintTileEntity;
import com.nmmoc7.polymercore.common.RegisterHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public class PolymerCoreBlueprintTileEntity extends TileEntity implements IPolymerCoreBlueprintTileEntity {
    IBlueprint blueprint;

    public PolymerCoreBlueprintTileEntity() {
        super(RegisterHandler.BLUEPRINT_TILE);
    }

    public void setBlueprint(IBlueprint blueprint) {
        this.blueprint = blueprint;
    }

    @Override
    public IBlueprint getBlueprint() {
        return blueprint;
    }

    @Override
    public boolean test() {
        for (Map.Entry<BlockPos, IBlueprintType> entry : getBlueprint().getMap().entrySet()) {
            if (entry.getValue().test(world.getBlockState(entry.getKey().add(getPos())))) {
                return false;
            }
        }

        return true;
    }
}
