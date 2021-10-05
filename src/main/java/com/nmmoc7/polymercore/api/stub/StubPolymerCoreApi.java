package com.nmmoc7.polymercore.api.stub;


import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.api.capability.IChunkMultiblockStorage;
import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import com.nmmoc7.polymercore.api.multiblock.assembled.IFreeMultiblock;
import com.nmmoc7.polymercore.api.registry.IMultiblockDefinitionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class StubPolymerCoreApi implements PolymerCoreApi {
    @Override
    public IMultiblockDefinitionManager getMultiblockManager() {
        throw new NotImplementedException("IMultiblockDefinitionManager");
    }

    @Override
    public Capability<IChunkMultiblockStorage> getChunkMultiblockCapability() {
        return null;
    }

    @Override
    public IAssembledMultiblock findMultiblock(World world, UUID id) {
        return null;
    }

    @Override
    public IAssembledMultiblock findMultiblock(World world, BlockPos pos, boolean coreBlock) {
        return null;
    }

    @Override
    public IAssembledMultiblock findMultiblock(World world, BlockPos pos) {
        return null;
    }

    @Override
    public Collection<IFreeMultiblock> findFreeMultiblocks(World world) {
        return Collections.emptyList();
    }
}
