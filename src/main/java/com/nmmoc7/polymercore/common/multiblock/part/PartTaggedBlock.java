package com.nmmoc7.polymercore.common.multiblock.part;

import net.minecraft.block.Block;
import net.minecraft.tags.ITag;

import java.util.List;

public class PartTaggedBlock extends PartBlockArray {
    public PartTaggedBlock(ITag.INamedTag<Block> tag, boolean allStatesAsSample) {
        super(tag.getAllElements(), allStatesAsSample);
    }

    public PartTaggedBlock(ITag.INamedTag<Block> tag) {
        this(tag, true);
    }
}
