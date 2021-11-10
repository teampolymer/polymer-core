package com.nmmoc7.polymercore.common.capability.chunk;

import com.nmmoc7.polymercore.PolymerCore;
import com.nmmoc7.polymercore.api.capability.IChunkMultiblockStorage;
import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.common.world.FreeMultiblockWorldSavedData;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class ChunkMultiblockStorage implements IChunkMultiblockStorage {
    /**
     * 映射结构
     * 方块坐标->(核心坐标, Part)
     */
    private final Map<BlockPos, Tuple<UUID, IMultiblockPart>> data = new HashMap<>();

    public ChunkMultiblockStorage(Chunk chunk) {
        this.chunk = chunk;
        this.multiblocks = new ArrayList<>();
    }

    private final Chunk chunk;
    private final List<UUID> multiblocks;

    public Map<BlockPos, Tuple<UUID, IMultiblockPart>> getData() {
        return Collections.unmodifiableMap(data);
    }


    @Override
    @Nullable
    public IChunk getChunk() {
        return chunk;
    }

    @Override
    @Nullable
    public Tuple<UUID, IMultiblockPart> getMultiblockPart(BlockPos pos) {
        return getData().get(pos);
    }

    @Override
    public void addMultiblock(UUID multiblockId, Map<BlockPos, IMultiblockPart> parts) {
        if (addMultiblockInternal(multiblockId, parts)) {
            multiblocks.add(multiblockId);
            chunk.markDirty();
        }
    }

    private boolean addMultiblockInternal(UUID multiblockId, Map<BlockPos, IMultiblockPart> parts) {
        boolean modified = false;
        for (Map.Entry<BlockPos, IMultiblockPart> entry : parts.entrySet()) {
            if (!isPosInChunk(entry.getKey())) {
                continue;
            }
            modified = true;
            Tuple<UUID, IMultiblockPart> replaced = data.put(entry.getKey(), new Tuple<>(multiblockId, entry.getValue()));

            if (replaced != null) {
                if (replaced.getA() != multiblockId) {
                    PolymerCore.LOG.warn("Position: {} Trying to replace an existing machine part for machine '{}' to another machine '{}', this may be a mistake!",
                        entry.getKey(), replaced.getA(), multiblockId);

                    IAssembledMultiblock duplicate = FreeMultiblockWorldSavedData.get(chunk.getWorld()).getAssembledMultiblock(multiblockId);
                    if (duplicate != null) {
                        duplicate.disassemble();
                    }
                }
            }
        }
        if (modified) {
            chunk.markDirty();
        }
        return modified;
    }

    @Override
    public void removeMultiblock(UUID multiblockId, Collection<BlockPos> blocks) {
        for (BlockPos pos : blocks) {
            if (isPosInChunk(pos)) {
                data.remove(pos);
            }
        }
        if (multiblocks.remove(multiblockId)) {
            chunk.markDirty();
        }
    }

    @Override
    public void removeMultiblock(UUID multiblockId) {
        Collection<BlockPos> blocks =
            data.entrySet().stream()
                .filter(it -> it.getValue().getA().equals(multiblockId))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        removeMultiblock(multiblockId, blocks);
    }

    @Override
    public void setContainingMultiblocks(List<UUID> uuidList) {
        this.multiblocks.clear();
        this.multiblocks.addAll(uuidList);
        chunk.markDirty();
    }

    @Override
    public List<UUID> getContainingMultiblocks() {
        return Collections.unmodifiableList(this.multiblocks);
    }

    @Override
    public void invalidate() {
        this.data.clear();
    }

    @Override
    public void initialize(World world) {
        this.data.clear();
        List<UUID> invalidateMultiblocks = new ArrayList<>();
        for (UUID uuid : this.multiblocks) {
            IAssembledMultiblock multiblock = FreeMultiblockWorldSavedData.get(world).getAssembledMultiblock(uuid);
            if (multiblock == null) {
                invalidateMultiblocks.add(uuid);
                continue;
            }
            addMultiblockInternal(uuid, multiblock.getParts());
        }
        if (PolymerCore.LOG.isDebugEnabled() && multiblocks.size() > 0) {
            PolymerCore.LOG.debug("Initializing {} machines in chunk {}", multiblocks.size(), chunk.getPos());
        }
        if (invalidateMultiblocks.size() > 0) {
            PolymerCore.LOG.warn("Initializing {} machines in chunk {} failed", invalidateMultiblocks.size(), chunk.getPos());

        }
        invalidateMultiblocks.forEach(multiblockId -> {
            removeMultiblock(multiblockId);
            PolymerCore.LOG.warn("Removing {} machines in chunk {}", multiblockId, chunk.getPos());
        });
    }
}
