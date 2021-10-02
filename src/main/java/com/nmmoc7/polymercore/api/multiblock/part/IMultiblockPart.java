package com.nmmoc7.polymercore.api.multiblock.part;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Collection;
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
    Collection<BlockState> getSampleBlocks();

    /**
     * 组合
     *
     * @param other 另一个IMultiblockPart
     * @return 组合后的方块
     */
    default IMultiblockPart combineWith(IMultiblockPart other) {
        return new CompositeMultiblockPart(this, other);
    }

    static IMultiblockPart combine(IMultiblockPart first, IMultiblockPart second) {
        if (first == null) {
            return second;
        }
        return first.combineWith(second);
    }

    /**
     * 检测一个方块是否匹配此Part
     * @param block 方块
     * @return 是否匹配
     */
    @Override
    boolean test(BlockState block);
}
