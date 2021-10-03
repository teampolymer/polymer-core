package com.nmmoc7.polymercore.api.capability;

import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.IChunk;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

public interface IChunkMultiblockStorage {
    IChunk getChunk();


    Map<BlockPos, Tuple<BlockPos, IMultiblockPart>> getData();
    @Nullable
    Tuple<BlockPos, IMultiblockPart> getMultiblockPart(BlockPos pos);

    void addMachine(BlockPos corePos, Map<BlockPos, IMultiblockPart> parts);

    void removeMachine(Collection<BlockPos> blocks);

    void removeMachine(BlockPos corePos);

    default boolean isPosInChunk(BlockPos pos) {
        ChunkPos chunkPos = getChunk().getPos();
        return chunkPos.x == pos.getX() >> 4 && chunkPos.z == pos.getZ() >> 4;
    }
}
