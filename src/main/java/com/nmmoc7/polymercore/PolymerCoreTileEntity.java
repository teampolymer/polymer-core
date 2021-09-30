package com.nmmoc7.polymercore;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class PolymerCoreTileEntity extends TileEntity implements IPolymerCoreTileEntity, ITickableTileEntity {
    public PolymerCoreTileEntity() {
        super(RegisterHandler.TILE);
    }

    @Override
    public void tick() {

    }
}
