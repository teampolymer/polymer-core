package com.nmmoc7.polymercore.api.multiblock.assembled;

import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import net.minecraft.util.math.ChunkPos;

import java.util.Collection;

public interface IFreeMultiblock extends IAssembledMultiblock {
    Collection<ChunkPos> getCrossedChunks();
}
