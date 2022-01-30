package com.teampolymer.polymer.core.api.manager;

import com.teampolymer.polymer.core.api.multiblock.IAssembledMultiblock;
import com.teampolymer.polymer.core.api.multiblock.IDefinedMultiblock;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface IWorldMultiblockManager {
    Optional<IAssembledMultiblock>  getById(UUID multiblockId);

    Optional<IAssembledMultiblock> getByCore(BlockPos corePos);

    Collection<IAssembledMultiblock> findAll();

    Collection<IAssembledMultiblock> findByChunks(ChunkPos ...poses);
}
