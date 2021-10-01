package com.nmmoc7.polymercore.common.block;

import com.nmmoc7.polymercore.api.block.IPolymerCoreBlueprintBlock;
import com.nmmoc7.polymercore.api.item.IBlueprintItem;
import com.nmmoc7.polymercore.api.item.IHammer;
import com.nmmoc7.polymercore.api.tileentity.IPolymerCoreBlueprintTileEntity;
import com.nmmoc7.polymercore.common.tileentity.blueprint.PolymerCoreBlueprintTileEntity;
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

public class PolymerCoreBlueprintBlock extends Block implements IPolymerCoreBlueprintBlock {
    public PolymerCoreBlueprintBlock() {
        super(AbstractBlock.Properties.create(Material.IRON, MaterialColor.IRON)
                .setRequiresTool()
                .hardnessAndResistance(5.0F, 6.0F)
                .sound(SoundType.METAL));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) return ActionResultType.PASS;

        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof IPolymerCoreBlueprintTileEntity) {
            IPolymerCoreBlueprintTileEntity blueTile = (IPolymerCoreBlueprintTileEntity) tile;
            ItemStack heldItem = player.getHeldItem(handIn);

            if (heldItem.getItem() instanceof IHammer) {
                boolean testResult = blueTile.testWithEvent();
                if (testResult) {
                    player.sendMessage(new StringTextComponent("111"), player.getUniqueID());
                }
            } else if (heldItem.getItem() instanceof IBlueprintItem) {
                blueTile.setBlueprint(((IBlueprintItem) heldItem.getItem()).getBlueprint());
            }
        }

        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new PolymerCoreBlueprintTileEntity();
    }
}
