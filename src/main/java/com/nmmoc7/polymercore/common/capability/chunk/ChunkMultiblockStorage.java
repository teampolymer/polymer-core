package com.nmmoc7.polymercore.common.capability.chunk;

import com.nmmoc7.polymercore.PolymerCore;
import com.nmmoc7.polymercore.api.capability.IChunkMultiblockStorage;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
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
    private Map<BlockPos, Tuple<BlockPos, IMultiblockPart>> data = new HashMap<>();

    public ChunkMultiblockStorage(Chunk chunk) {
        this.chunk = chunk;
    }

    private final Chunk chunk;

    public Map<BlockPos, Tuple<BlockPos, IMultiblockPart>> getData() {
        return data;
    }


    @Override
    @Nullable
    public IChunk getChunk() {
        return chunk;
    }

    @Override
    @Nullable
    public Tuple<BlockPos, IMultiblockPart> getMultiblockPart(BlockPos pos) {
        return data.get(pos);
    }

    @Override
    public void addMachine(BlockPos corePos, Map<BlockPos, IMultiblockPart> parts) {
        boolean modified = false;
        for (Map.Entry<BlockPos, IMultiblockPart> entry : parts.entrySet()) {
            if (!isPosInChunk(entry.getKey())) {
                continue;
            }
            Tuple<BlockPos, IMultiblockPart> replaced = data.put(entry.getKey(), new Tuple<>(corePos, entry.getValue()));
            if (replaced != null) {
                modified = true;
                if (replaced.getA() != corePos) {
                    PolymerCore.LOG.warn("Position: {} Trying to replace an existing machine part for machine in '{}' to another machine in '{}', this may be a mistake!",
                        entry.getKey(), replaced.getA(), corePos);
                }
            }
        }
        if (modified) {
            chunk.markDirty();
        }
    }

    @Override
    public void removeMachine(Collection<BlockPos> blocks) {
        boolean modified = false;
        for (BlockPos pos : blocks) {
            if (isPosInChunk(pos)) {
                if (data.remove(pos) != null) {
                    modified = true;
                }
            }
        }
        if (modified) {
            chunk.markDirty();
        }
    }

    @Override
    public void removeMachine(BlockPos corePos) {
        Collection<BlockPos> blocks =
            data.entrySet().stream()
                .filter(it -> it.getValue().getA().equals(corePos))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        removeMachine(blocks);
    }
}
