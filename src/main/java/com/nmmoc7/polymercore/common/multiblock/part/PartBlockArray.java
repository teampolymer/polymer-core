package com.nmmoc7.polymercore.common.multiblock.part;

import com.google.common.collect.ImmutableList;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tags.ITag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PartBlockArray implements IMultiblockPart {
    private final List<Block> blocks;
    private final List<BlockState> samples;

    public PartBlockArray(List<Block> blocks) {
        this(blocks, true);
    }

    /**
     * 可以匹配多个方块的结构部件
     *
     * @param blocks            可能的方块列表
     * @param allStatesAsSample 是否将所有的blockState作为预览样本
     */
    public PartBlockArray(List<Block> blocks, boolean allStatesAsSample) {
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
    public BlockState getFirstMatchingBlock() {
        return blocks.get(0).getDefaultState();
    }

    @Override
    public Collection<BlockState> getSampleBlocks() {
        return samples;
    }

    @Override
    public boolean test(BlockState block) {
        final Block condition = block.getBlock();
        return blocks.contains(condition);
    }
}
