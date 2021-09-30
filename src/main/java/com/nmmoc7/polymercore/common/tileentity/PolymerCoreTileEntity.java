package com.nmmoc7.polymercore.common.tileentity;

import com.nmmoc7.polymercore.api.tileentity.IPolymerCoreTileEntity;
import com.nmmoc7.polymercore.common.RegisterHandler;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class PolymerCoreTileEntity extends TileEntity implements IPolymerCoreTileEntity, ITickableTileEntity {
    public PolymerCoreTileEntity() {
        super(RegisterHandler.TILE);
    }

    @Override
    public void tick() {

    }
}
