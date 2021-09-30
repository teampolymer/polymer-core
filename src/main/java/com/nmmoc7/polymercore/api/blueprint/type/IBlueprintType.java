package com.nmmoc7.polymercore.api.blueprint.type;

import net.minecraft.block.BlockState;

public interface IBlueprintType {
    boolean test(BlockState block);
}
