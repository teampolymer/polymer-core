package com.nmmoc7.polymercore.common.multiblock.free;

import com.nmmoc7.polymercore.PolymerCore;
import com.nmmoc7.polymercore.api.capability.IChunkMultiblockStorage;
import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.IMultiblockType;
import com.nmmoc7.polymercore.api.multiblock.extension.IMultiblockExtension;
import com.nmmoc7.polymercore.common.capability.chunk.CapabilityChunkMultiblockStorage;
import com.nmmoc7.polymercore.common.multiblock.assembled.FreeMultiblockImpl;
import com.nmmoc7.polymercore.common.world.FreeMultiblockWorldSavedData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class MultiblockTypeFree extends ForgeRegistryEntry<IMultiblockType> implements IMultiblockType {
    @Override
    public IAssembledMultiblock createMultiblockIn(IDefinedMultiblock definition, World world, BlockPos pos, Rotation rotation, boolean isSymmetrical, List<Tuple<IMultiblockExtension, Integer>> appliedExtensions) {
        return null;
    }

    @Override
    public IAssembledMultiblock createMultiblockIn(IDefinedMultiblock definition, World world, BlockPos pos, Rotation rotation, boolean isSymmetrical) {
        UUID uuid = UUID.randomUUID();
        FreeMultiblockImpl multiblock = new FreeMultiblockImpl(
            uuid,
            pos,
            isSymmetrical,
            rotation,
            definition
        );
        FreeMultiblockWorldSavedData.get(world).addAssembledMultiblock(multiblock);

        Collection<ChunkPos> crossedChunks = multiblock.getCrossedChunks();
        for (ChunkPos chunkPos : crossedChunks) {
            Chunk chunk = world.getChunk(chunkPos.x, chunkPos.z);
            LazyOptional<IChunkMultiblockStorage> capability = chunk.getCapability(CapabilityChunkMultiblockStorage.MULTIBLOCK_STORAGE);
            capability.ifPresent(it -> it.addMultiblock(uuid, multiblock.getParts()));
        }

        return multiblock;
    }

    @Override
    public IAssembledMultiblock createFromNBT(CompoundNBT nbt) {
        //TODO: 拓展
        FreeMultiblockImpl multiblock = new FreeMultiblockImpl();
        try {
            multiblock.deserializeNBT(nbt);
        }catch (IllegalStateException e) {
            PolymerCore.LOG.error(e.getMessage());
            return null;
        }
        return multiblock;
    }
}
