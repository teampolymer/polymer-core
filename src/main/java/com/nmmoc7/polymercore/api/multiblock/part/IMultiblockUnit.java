package com.nmmoc7.polymercore.api.multiblock.part;

import com.nmmoc7.polymercore.api.multiblock.MultiblockDirection;
import net.minecraft.block.BlockState;

import java.util.List;
import java.util.function.Predicate;

/**
 * 多方快结构的最小单元，一般是一个或一种类型的方块
 */
public interface IMultiblockUnit extends Predicate<BlockState> {
    /**
     * 获取可能的第一个方块
     *
     * @return 方块
     */
    default BlockState getSampleBlock() {
        return getSampleBlocks().get(0);
    }

    /**
     * 获取这个部件可能的方块的样本
     *
     * @return 样本的方块列表
     */
    List<BlockState> getSampleBlocks();

    IMultiblockUnit withDirection(MultiblockDirection direction);

    /**
     * 检测一个方块是否匹配此Part
     *
     * @param block 方块
     * @return 是否匹配
     */
    @Override
    boolean test(BlockState block);
}
