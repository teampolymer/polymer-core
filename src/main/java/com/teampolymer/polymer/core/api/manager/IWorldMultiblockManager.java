package com.teampolymer.polymer.core.api.manager;

import com.teampolymer.polymer.core.api.multiblock.IAssembledMultiblock;
import com.teampolymer.polymer.core.api.multiblock.assembled.IWorldMultiblock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface IWorldMultiblockManager {
    Optional<IAssembledMultiblock> getById(World world, UUID multiblockId);


    Optional<IAssembledMultiblock> getByPosition(World world, BlockPos corePos, boolean isCore);

    /**
     * 获取世界上所有的多方快结构
     * 警告：获取的多方快可能在未加载区块或没有初始化
     */
    Collection<? extends IAssembledMultiblock> findAll(World world);

    /**
     * 获取区块内的上所有的多方快结构
     * 仅判断核心方块所在区块
     * 警告：获取的多方快可能在未加载区块或没有初始化
     */
    Collection<? extends IAssembledMultiblock> findByChunks(World world, ChunkPos... poses);
}
