package com.nmmoc7.polymercore.api.multiblock.builder;

import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.state.Property;
import net.minecraft.tags.ITag;
import net.minecraft.tags.Tag;

import java.util.Map;

public interface IUnitFactory {
    IMultiblockUnit createByBlock(Block block);

    IMultiblockUnit createByBlock(String block);

    IMultiblockUnit createByFluid(Fluid fluid);

    IMultiblockUnit createByFluid(String fluid);

    IMultiblockUnit createByBlockTag(ITag<Block> tag);
    IMultiblockUnit createByFluidTag(ITag<Fluid> tag);

    IMultiblockUnit createByTag(String tag);

    IMultiblockUnit createByProperties(Block block, Map<String, String>  properties);

    IMultiblockUnit createByProperties(String block, Map<String, String> properties);

    IMultiblockUnit combine(IMultiblockUnit ...units);

}
