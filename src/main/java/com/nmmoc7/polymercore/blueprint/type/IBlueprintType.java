package com.nmmoc7.polymercore.blueprint.type;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

public interface IBlueprintType {
    boolean test(BlockState block);
}
