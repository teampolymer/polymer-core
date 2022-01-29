package com.teampolymer.polymer.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

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
