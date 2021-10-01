package com.nmmoc7.polymercore.api.blueprint;

import com.nmmoc7.polymercore.api.blueprint.type.IBlueprintType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Map;

public interface IBlueprint extends IForgeRegistryEntry<IBlueprint> {
    Map<BlockPos, IBlueprintType> getMap();
    BlockPos getCorePos();
    IBlueprintType getCoreType();
}
