package com.nmmoc7.polymercore.api.capability;

import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IChunkMultiblockStorage {
    IChunk getChunk();


    Map<BlockPos, Tuple<UUID, IMultiblockUnit>> getData();

    @Nullable
    Tuple<UUID, IMultiblockUnit> getMultiblockPart(BlockPos pos);

    void addMultiblock(UUID multiblockId, Map<BlockPos, IMultiblockUnit> parts);

    void removeMultiblock(UUID multiblockId, Collection<BlockPos> blocks);

    void removeMultiblock(UUID multiblockId);

    default boolean isPosInChunk(BlockPos pos) {
        ChunkPos chunkPos = getChunk().getPos();
        return chunkPos.x == pos.getX() >> 4 && chunkPos.z == pos.getZ() >> 4;
    }

    void setContainingMultiblocks(List<UUID> uuidList);
    List<UUID> getContainingMultiblocks();

    void invalidate();

    void initialize(World world);
}
