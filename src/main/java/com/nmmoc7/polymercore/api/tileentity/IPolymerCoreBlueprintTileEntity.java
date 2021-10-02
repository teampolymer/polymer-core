package com.nmmoc7.polymercore.api.tileentity;

//import com.nmmoc7.polymercore.api.blueprint.IBlueprint;
import com.nmmoc7.polymercore.common.event.PolymerTestEvent;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;

public abstract class IPolymerCoreBlueprintTileEntity extends TileEntity {
    public IPolymerCoreBlueprintTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    abstract protected boolean test();

    public final boolean testWithEvent() {
//        MinecraftForge.EVENT_BUS.post(new PolymerTestEvent.Start(getBlueprint()));
//        boolean result = test();
//        MinecraftForge.EVENT_BUS.post(new PolymerTestEvent.End(getBlueprint(), result));
//        return result;
        return false;
    }
}
