package com.teampolymer.polymer.core.api.multiblock.assembled;

import com.teampolymer.polymer.core.api.multiblock.IAssembledMultiblock;
import net.minecraft.util.math.ChunkPos;

import java.util.Collection;

public interface IWorldMultiblock extends IAssembledMultiblock {
    Collection<ChunkPos> getCrossedChunks();
}
