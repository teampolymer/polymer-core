package com.nmmoc7.polymercore.common.blueprint.type;

import com.nmmoc7.polymercore.api.blueprint.type.IBlueprintType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tags.ITag;

import java.util.Arrays;

public class TagBlueprintType implements IBlueprintType {
    Block[] blocks;

    public TagBlueprintType(ITag.INamedTag<Block> tag) {
        blocks = tag.getAllElements().toArray(new Block[0]);
    }

    @Override
    public boolean test(BlockState block) {
        return Arrays.stream(this.blocks).anyMatch(obj -> obj == block.getBlock());
    }
}
