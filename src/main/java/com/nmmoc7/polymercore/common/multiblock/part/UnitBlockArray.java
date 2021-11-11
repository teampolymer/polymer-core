package com.nmmoc7.polymercore.common.multiblock.part;

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
     * @param blocks            可能的方块列表
     * @param allStatesAsSample 是否将所有的blockState作为预览样本
     */
    public UnitBlockArray(List<Block> blocks, boolean allStatesAsSample) {
        super(null);
        this.blocks = ImmutableList.copyOf(blocks);
        if (allStatesAsSample) {
            this.samples = blocks.stream()
                .flatMap(it ->
                    it.getStateContainer()
                        .getValidStates()
                        .stream()
                ).collect(Collectors.toList());
        } else {
            this.samples = blocks.stream()
                .map(Block::getDefaultState)
                .collect(Collectors.toList());
        }

    }

    @Override
    public boolean test(BlockState block) {
        final Block condition = block.getBlock();
        return blocks.contains(condition);
    }
}
