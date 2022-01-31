package com.teampolymer.polymer.core.common.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.teampolymer.polymer.core.api.manager.IArchetypeMultiblockManager;
import com.teampolymer.polymer.core.api.multiblock.IArchetypeMultiblock;
import com.teampolymer.polymer.core.api.multiblock.MultiblockDirection;
import com.teampolymer.polymer.core.api.multiblock.part.IMultiblockPart;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class MultiblockManagerImpl implements IArchetypeMultiblockManager {
    private final BiMap<ResourceLocation, IArchetypeMultiblock> multiblocks = HashBiMap.create();
    private final Multimap<String, IArchetypeMultiblock> tagsMap = HashMultimap.create();


    @Override
    public Optional<IArchetypeMultiblock> findById(ResourceLocation location) {
        return Optional.ofNullable(multiblocks.get(location));
    }

    @Override
    public Collection<IArchetypeMultiblock> findByCore(BlockState coreBlock) {
        return multiblocks.values().stream()
            .filter(it -> {
                IMultiblockPart core = it.getCore();
                return core != null && core.pickupChoice(coreBlock, MultiblockDirection.NONE) != null;
            }).collect(Collectors.toList());
    }

    @Override
    public Collection<IArchetypeMultiblock> findAll() {
        return Collections.unmodifiableCollection(multiblocks.values());
    }

    @Override
    public Collection<IArchetypeMultiblock> findByTag(String tag) {
        return tagsMap.get(tag);
    }

    @Override
    public void register(IArchetypeMultiblock multiblock) {
        Objects.requireNonNull(multiblock.getRegistryName(), "Registry Name cannot be null");
        multiblocks.put(multiblock.getRegistryName(), multiblock);
        for (String tag : multiblock.getTags()) {
            tagsMap.put(tag, multiblock);
        }
    }
}
