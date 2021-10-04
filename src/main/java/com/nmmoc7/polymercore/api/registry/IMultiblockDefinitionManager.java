package com.nmmoc7.polymercore.api.registry;

import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface IMultiblockDefinitionManager {
    Optional<IDefinedMultiblock> getDefinedMultiblock(ResourceLocation location);

    Collection<IDefinedMultiblock> getDefinedMultiblocksForCore(BlockState coreBlock);

    Collection<IDefinedMultiblock> getDefinedMultiblocks();

    Collection<IDefinedMultiblock> getDefinedMultiblocks(String tag);
    void addDefinedMultiblock(IDefinedMultiblock multiblock);
}
