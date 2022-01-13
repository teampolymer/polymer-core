package com.nmmoc7.polymercore.common.block;

import com.nmmoc7.polymercore.common.IRestorableTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

import java.util.Collections;
import java.util.List;

import net.minecraft.block.AbstractBlock.Properties;

public class TestBlock extends Block {
    public TestBlock() {
        super(Properties.of(Material.STONE));
    }

//    @Override
//    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
//        TileEntity tileEntity = builder.get(LootParameters.BLOCK_ENTITY);
//        if (tileEntity instanceof IRestorableTileEntity) {
//            ItemStack stack = new ItemStack(this, 1);
//            CompoundNBT tagCompound = new CompoundNBT();
//            ((IRestorableTileEntity) tileEntity).writeRestorableToNBT(tagCompound);
//            stack.setTag(tagCompound);
//            return Collections.singletonList(stack);
//        } else {
//            return super.getDrops(state, builder);
//        }
//    }
}
