package com.nmmoc7.polymercore.common.multiblock.builder;

import com.nmmoc7.polymercore.api.multiblock.builder.IUnitFactory;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
import com.nmmoc7.polymercore.api.util.BlockStateUtils;
import com.nmmoc7.polymercore.common.multiblock.unit.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Map;

public class DefaultUnitFactory implements IUnitFactory {
    @Override
    public IMultiblockUnit createByBlock(Block block) {
        return new UnitSpecifiedBlock(block);
    }

    @Override
    public IMultiblockUnit createByBlock(String block) {
        Block value = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryCreate(block));
        return createByBlock(value);
    }

    @Override
    public IMultiblockUnit createByFluid(Fluid fluid) {
        return new UnitSpecifiedFluid(fluid);
    }

    @Override
    public IMultiblockUnit createByFluid(String fluid) {
        Fluid value = ForgeRegistries.FLUIDS.getValue(ResourceLocation.tryCreate(fluid));
        return createByFluid(value);
    }

    @Override
    public <T> IMultiblockUnit createByTag(ITag<T> tag) {
        ResourceLocation id = BlockTags.getCollection().getDirectIdFromTag((ITag<Block>) tag);
        if (id == null) {
            if (FluidTags.getCollection().getDirectIdFromTag((ITag<Fluid>) tag) == null) {
                return null;
            }
            List<Fluid> elements = (List<Fluid>) tag.getAllElements();
            if (elements.isEmpty()) {
                return null;
            }
            if (elements.size() == 1) {
                return createByFluid(elements.get(0));
            }
            return new UnitFluidArray(elements);

        } else {
            List<Block> elements = (List<Block>) tag.getAllElements();
            if (elements.isEmpty()) {
                return null;
            }
            if (elements.size() == 1) {
                return createByBlock(elements.get(0));
            }
            return new UnitBlockArray(elements);
        }

    }

    @Override
    public IMultiblockUnit createByTag(String tag) {
        ResourceLocation location = ResourceLocation.tryCreate(tag);
        if (location == null) {
            return null;
        }
        ITag<Block> blockTag = BlockTags.getCollection().get(location);
        if (blockTag != null) {
            return createByTag(blockTag);
        }
        ITag<Fluid> fluidTag = FluidTags.getCollection().get(location);
        if (fluidTag != null) {
            return createByTag(fluidTag);
        }
        return null;
    }

    @Override
    public IMultiblockUnit createByProperties(Block block, Map<String, String> properties) {
        List<BlockState> states = BlockStateUtils.getAllMatchingProperties(block, properties);
        if (states.size() == 1) {
            return new UnitBlockState(states.get(0));
        }
        if (states.isEmpty()) {
            return new UnitBlockState(BlockStateUtils.getWithProperties(block, properties));
        }
        return new UnitBlockStateArray(states);
    }

    @Override
    public IMultiblockUnit createByProperties(String block, Map<String, String> properties) {
        Block value = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryCreate(block));
        if (value == null)
            return null;
        return createByProperties(value, properties);
    }

    @Override
    public IMultiblockUnit combine(IMultiblockUnit... units) {
        if (units.length == 0) {
            return null;
        }
        if (units.length == 1) {
            return units[0];
        }
        return new DefaultCompositeUnit(units);
    }
}
