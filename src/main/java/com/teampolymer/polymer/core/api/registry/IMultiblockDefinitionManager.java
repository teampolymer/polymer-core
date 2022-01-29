package com.teampolymer.polymer.core.api.registry;

import com.teampolymer.polymer.core.api.multiblock.IDefinedMultiblock;
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
