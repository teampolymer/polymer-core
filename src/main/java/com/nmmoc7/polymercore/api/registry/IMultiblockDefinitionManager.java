package com.nmmoc7.polymercore.api.registry;

import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.Optional;

public interface IMultiblockDefinitionManager {
    Optional<IDefinedMultiblock> findById(ResourceLocation location);

    Collection<IDefinedMultiblock> findByCore(BlockState coreBlock);

    Collection<IDefinedMultiblock> findAll();

    Collection<IDefinedMultiblock> findByTag(String tag);
    void register(IDefinedMultiblock multiblock);
}
