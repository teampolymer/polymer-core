package com.nmmoc7.polymercore.common.blueprint.type;

import com.nmmoc7.polymercore.api.blueprint.type.IBlueprintType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.util.Arrays;

public class BlockArrBlueprintType implements IBlueprintType {
    Block[] blocks;

    public BlockArrBlueprintType(Block... blocks) {
        this.blocks = blocks;
    }

    @Override
    public boolean test(BlockState block) {
        return Arrays.stream(blocks).anyMatch(block1 -> block.getBlock() == block1);
    }
}
