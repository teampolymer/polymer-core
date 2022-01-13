package com.nmmoc7.polymercore.common.multiblock;

import com.nmmoc7.polymercore.api.component.IMultiblockComponent;
import com.nmmoc7.polymercore.api.machine.IMachine;
import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.IMultiblockType;
import com.nmmoc7.polymercore.api.multiblock.MultiblockDirection;
import com.nmmoc7.polymercore.api.multiblock.assembled.IMultiblockAssembleRule;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.multiblock.part.IPartChoice;
import com.nmmoc7.polymercore.api.multiblock.part.IPartLimitConfig;
import com.nmmoc7.polymercore.api.util.PositionUtils;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DefinedMultiblockImpl extends AbstractMultiblock implements IDefinedMultiblock {
    private ResourceLocation registryName = null;
    private final Map<Vector3i, IMultiblockPart> partsMap;
    private final IMultiblockType type;
    private final boolean canSymmetrical;
    private final List<String> tags;
    private Collection<IPartLimitConfig> limitConfigs;

    public DefinedMultiblockImpl(List<IMultiblockComponent> components, String machine, Vector3i size, Map<Vector3i, IMultiblockPart> partsMap, IMultiblockType type, boolean canSymmetrical, List<String> tags, Collection<IPartLimitConfig> limitConfigs) {
        super(components, machine, size);
        this.partsMap = partsMap;
        this.type = type;
        this.canSymmetrical = canSymmetrical;
        this.tags = tags;
        this.limitConfigs = limitConfigs;
    }


    @Nullable
    @Override
    public IAssembledMultiblock assemble(@NotNull World world, @NotNull BlockPos coreOffset, @NotNull Rotation rotation, boolean isSymmetrical) {
        BlockPos corePos = coreOffset.immutable();
        IMultiblockAssembleRule rule = getType().createEmptyRule(corePos, rotation, isSymmetrical);
        if (!canAssemble(world, coreOffset, rotation, isSymmetrical, rule)) {
            return null;
        }
        return getType().createMultiblockIn(this, world, rule);
    }

    @Nullable
    @Override
    public IAssembledMultiblock assemble(@NotNull World world, @NotNull BlockPos coreOffset, @NotNull Rotation rotation) {
        if (canAssemble(world, coreOffset, rotation, true)) {
            return assemble(world, coreOffset, rotation, true);
        }
        return assemble(world, coreOffset, rotation, false);
    }

    @Nullable
    @Override
    public IAssembledMultiblock assemble(@NotNull World world, @NotNull BlockPos coreOffset) {
        for (Rotation value : Rotation.values()) {
            boolean result = canAssemble(world, coreOffset, value);
            if (result) {
                return assemble(world, coreOffset, value);
            }
        }
        return null;
    }

    @Override
    public boolean canAssemble(@NotNull World world, @NotNull BlockPos coreOffset, @NotNull Rotation rotation, boolean isSymmetrical, IMultiblockAssembleRule ruleToFill) {
        if (!canSymmetrical && isSymmetrical) {
            return false;
        }
        Map<Vector3i, IMultiblockPart> parts = getParts();
        for (Map.Entry<Vector3i, IMultiblockPart> entry : parts.entrySet()) {
            BlockPos testPos = PositionUtils.applyModifies(entry.getKey(), coreOffset, rotation, isSymmetrical);
            BlockState block = world.getBlockState(testPos);
            IPartChoice choice = entry.getValue().pickupChoice(block, MultiblockDirection.get(rotation, isSymmetrical));
            if (choice == null) {
                return false;
            }
            if (choice.getType() != null) {
                ruleToFill.makeChoice(entry.getKey(), choice);
            }

        }
        return true;
    }

    @Override
    public boolean canAssemble(@NotNull World world, @NotNull BlockPos coreOffset, @NotNull Rotation rotation, boolean isSymmetrical) {
        return canAssemble(world, coreOffset, rotation, isSymmetrical, getType().createEmptyRule(coreOffset, rotation, isSymmetrical));
    }

    @Override
    public boolean canAssemble(@NotNull World world, @NotNull BlockPos coreOffset, @NotNull Rotation rotation) {
        return canAssemble(world, coreOffset, rotation, true) || canAssemble(world, coreOffset, rotation, false);
    }

    @Override
    public boolean canAssemble(@NotNull World world, @NotNull BlockPos coreOffset) {
        for (Rotation value : Rotation.values()) {
            boolean result = canAssemble(world, coreOffset, value);
            if (result) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canSymmetrical() {
        return canSymmetrical;
    }

    @Override
    public IMultiblockType getType() {
        return type;
    }

    @Override
    public Map<Vector3i, IMultiblockPart> getParts() {
        return partsMap;
    }

    @Override
    public Collection<String> getTags() {
        return Collections.unmodifiableList(this.tags);
    }

    @Override
    public Collection<IPartLimitConfig> getLimitConfigs() {
        return limitConfigs;
    }

    @Override
    public DefinedMultiblockImpl setRegistryName(ResourceLocation name) {
        registryName = name;
        return this;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return registryName;
    }

    @Override
    public IMultiblockPart getCore() {
        return partsMap.get(Vector3i.ZERO);
    }
}
