package com.nmmoc7.polymercore.common.blueprint.type;

import com.nmmoc7.polymercore.api.blueprint.type.IBlueprintType;
import net.minecraft.block.BlockState;

import java.util.Arrays;

public class BlockStateArrBlueprintType implements IBlueprintType {
    BlockState[] blockStates;

    public BlockStateArrBlueprintType(BlockState... blockStates) {
        this.blockStates = blockStates;
    }

    @Override
    public boolean test(BlockState block) {
        return Arrays.stream(blockStates).anyMatch(state -> block == state);
    }
}
