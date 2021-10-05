package com.nmmoc7.polymercore.api.multiblock.part;

import net.minecraft.block.BlockState;

import java.util.List;
import java.util.function.Predicate;

public interface IMultiblockPart extends Predicate<BlockState> {
    /**
     * 获取可能的第一个方块
     *
     * @return 方块
     */
    BlockState getFirstMatchingBlock();

    /**
     * 获取这个部件可能的方块的样本
     *
     * @return 样本的方块列表
     */
    List<BlockState> getSampleBlocks();

    /**
     * 检测一个方块是否匹配此Part
     * @param block 方块
     * @return 是否匹配
     */
    @Override
    boolean test(BlockState block);
}
