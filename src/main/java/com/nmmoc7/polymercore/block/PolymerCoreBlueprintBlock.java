package com.nmmoc7.polymercore.block;

import com.nmmoc7.polymercore.tileentity.blueprint.PolymerCoreBlueprintTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class PolymerCoreBlueprintBlock extends Block implements IPolymerCoreBlueprintBlock {
    public PolymerCoreBlueprintBlock() {
        super(AbstractBlock.Properties.create(Material.IRON, MaterialColor.IRON)
                .setRequiresTool()
                .hardnessAndResistance(5.0F, 6.0F)
                .sound(SoundType.METAL));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PolymerCoreBlueprintTileEntity();
    }
}
