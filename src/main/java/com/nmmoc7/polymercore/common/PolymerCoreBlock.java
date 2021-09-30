package com.nmmoc7.polymercore.common;

import com.nmmoc7.polymercore.api.IPolymerCoreBlock;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class PolymerCoreBlock extends Block implements IPolymerCoreBlock {
    public PolymerCoreBlock() {
        super(AbstractBlock.Properties.create(Material.IRON, MaterialColor.IRON)
                .setRequiresTool()
                .hardnessAndResistance(5.0F, 6.0F)
                .sound(SoundType.METAL));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new PolymerCoreTileEntity();
    }
}
