package com.nmmoc7.polymercore.common.multiblock.unit;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.util.List;
import java.util.stream.Collectors;

public class UnitBlockArray extends AbstractUnit {
    private final List<Block> blocks;

    /**
     * 可以匹配多个方块的结构部件
     *
     * @param blocks 可能的方块列表
     */
    public UnitBlockArray(List<Block> blocks) {
        super(null);
        this.blocks = ImmutableList.copyOf(blocks);
        this.samples = blocks.stream()
            .map(Block::defaultBlockState)
            .collect(Collectors.toList());
    }

    @Override
    public boolean test(BlockState block) {
        final Block condition = block.getBlock();
        return blocks.contains(condition);
    }
}
