package com.nmmoc7.polymercore.common.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.registry.IMultiblockDefinitionManager;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class MultiblockManagerImpl implements IMultiblockDefinitionManager {
    private final BiMap<ResourceLocation, IDefinedMultiblock> multiblocks = HashBiMap.create();
    private final Multimap<String, IDefinedMultiblock> tagsMap = HashMultimap.create();


    @Override
    public Optional<IDefinedMultiblock> getDefinedMultiblock(ResourceLocation location) {
        return Optional.ofNullable(multiblocks.get(location));
    }

    @Override
    public Collection<IDefinedMultiblock> getDefinedMultiblocksForCore(BlockState coreBlock) {
        return multiblocks.values().stream()
            .filter(it -> {
                IMultiblockPart core = it.getCore();
                return core != null && core.pickupChoice(coreBlock) != null;
            }).collect(Collectors.toList());
    }

    @Override
    public Collection<IDefinedMultiblock> getDefinedMultiblocks() {
        return Collections.unmodifiableCollection(multiblocks.values());
    }

    @Override
    public Collection<IDefinedMultiblock> getDefinedMultiblocks(String tag) {
        return tagsMap.get(tag);
    }

    @Override
    public void addDefinedMultiblock(IDefinedMultiblock multiblock) {
        Objects.requireNonNull(multiblock.getRegistryName(), "Registry Name cannot be null");
        multiblocks.put(multiblock.getRegistryName(), multiblock);
        for (String tag : multiblock.getTags()) {
            tagsMap.put(tag, multiblock);
        }
    }
}
