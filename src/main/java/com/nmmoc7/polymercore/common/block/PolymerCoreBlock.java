package com.nmmoc7.polymercore.common.block;

import com.nmmoc7.polymercore.api.block.IPolymerCoreBlock;
import com.nmmoc7.polymercore.api.item.IBlueprintItem;
import com.nmmoc7.polymercore.api.item.IHammer;
import com.nmmoc7.polymercore.api.tileentity.IPolymerCoreBlueprintTileEntity;
import com.nmmoc7.polymercore.common.tileentity.PolymerCoreTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

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
