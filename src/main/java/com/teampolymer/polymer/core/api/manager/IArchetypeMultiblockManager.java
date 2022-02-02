package com.teampolymer.polymer.core.api.manager;

import com.teampolymer.polymer.core.api.multiblock.IArchetypeMultiblock;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.Optional;

public interface IArchetypeMultiblockManager {
    Optional<IArchetypeMultiblock> findById(ResourceLocation location);

    Collection<IArchetypeMultiblock> findByCore(BlockState coreBlock);

    Collection<IArchetypeMultiblock> findAll();

    Collection<IArchetypeMultiblock> findByTag(String tag);
    void register(IArchetypeMultiblock multiblock);
}
