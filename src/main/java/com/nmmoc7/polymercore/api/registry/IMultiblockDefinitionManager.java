package com.nmmoc7.polymercore.api.registry;

import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.Optional;

public interface IMultiblockDefinitionManager {
    Optional<IDefinedMultiblock> getDefinedMultiblock(ResourceLocation location);

    Collection<IDefinedMultiblock> getDefinedMultiblocksForCore(BlockState coreBlock);

    Collection<IDefinedMultiblock> getDefinedMultiblocks();

    Collection<IDefinedMultiblock> getDefinedMultiblocks(String tag);
    void addDefinedMultiblock(IDefinedMultiblock multiblock);
}
