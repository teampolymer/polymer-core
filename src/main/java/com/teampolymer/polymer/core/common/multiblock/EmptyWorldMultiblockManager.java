package com.teampolymer.polymer.core.common.multiblock;

import com.teampolymer.polymer.core.api.manager.IWorldMultiblockManager;
import com.teampolymer.polymer.core.api.multiblock.IAssembledMultiblock;
import com.teampolymer.polymer.core.api.multiblock.assembled.IWorldMultiblock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

public class EmptyWorldMultiblockManager implements IWorldMultiblockManager {

    @Override
    public Optional<IAssembledMultiblock> getById(World world, UUID multiblockId) {
        return Optional.empty();
    }

    @Override
    public Optional<IAssembledMultiblock> getByPosition(World world, BlockPos corePos, boolean isCore) {
        return Optional.empty();
    }

    @Override
    public Collection<? extends IAssembledMultiblock> findAll(World world) {
        return Collections.emptyList();
    }

    @Override
    public Collection<? extends IAssembledMultiblock> findByChunks(World world, ChunkPos... poses) {
        return Collections.emptyList();
    }
}
