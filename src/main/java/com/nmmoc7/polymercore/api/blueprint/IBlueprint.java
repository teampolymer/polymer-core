package com.nmmoc7.polymercore.api.blueprint;

import com.nmmoc7.polymercore.api.blueprint.type.IBlueprintType;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public interface IBlueprint {
    Map<BlockPos, IBlueprintType> getMap();
}
