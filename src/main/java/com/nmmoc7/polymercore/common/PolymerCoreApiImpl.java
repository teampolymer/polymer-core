package com.nmmoc7.polymercore.common;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.api.capability.IChunkMultiblockStorage;
import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import com.nmmoc7.polymercore.api.multiblock.assembled.IFreeMultiblock;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
import com.nmmoc7.polymercore.api.registry.IMultiblockDefinitionManager;
import com.nmmoc7.polymercore.common.capability.chunk.CapabilityChunkMultiblockStorage;
import com.nmmoc7.polymercore.common.registry.MultiblockManagerImpl;
import com.nmmoc7.polymercore.common.world.FreeMultiblockWorldSavedData;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Lazy;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class PolymerCoreApiImpl implements PolymerCoreApi {
    private final Lazy<IMultiblockDefinitionManager> multiblockDefinitionManager = Lazy.of(MultiblockManagerImpl::new);

    @Override
    public IMultiblockDefinitionManager getMultiblockManager() {
        return multiblockDefinitionManager.get();
    }

    @Override
    public Capability<IChunkMultiblockStorage> getChunkMultiblockCapability() {
        return CapabilityChunkMultiblockStorage.MULTIBLOCK_STORAGE;
    }

    @Override
    public IAssembledMultiblock findMultiblock(World world, UUID id) {
        return FreeMultiblockWorldSavedData.get(world).getAssembledMultiblock(id);
    }

    @Override
    public IAssembledMultiblock findMultiblock(World world, BlockPos pos, boolean coreBlock) {
        if (world.isClientSide) {
            return null;
        }
        if (coreBlock) {
            return FreeMultiblockWorldSavedData.get(world).getAssembledMultiblock(pos);
        }

        Tuple<UUID, IMultiblockUnit> part = CapabilityChunkMultiblockStorage.getMultiblockPart(world, pos);
        if (part == null) {
            return null;
        }
        return FreeMultiblockWorldSavedData.get(world).getAssembledMultiblock(part.getA());
    }

    @Override
    public IAssembledMultiblock findMultiblock(World world, BlockPos pos) {
        return findMultiblock(world, pos, false);
    }

    @Override
    public Collection<IFreeMultiblock> findFreeMultiblocks(World world) {
        if (world.isClientSide) {
            return Collections.emptyList();
        }
        return FreeMultiblockWorldSavedData.get(world).getAssembledMultiblocks();
    }
}
