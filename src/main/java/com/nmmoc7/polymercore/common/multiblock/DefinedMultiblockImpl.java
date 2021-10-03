package com.nmmoc7.polymercore.common.multiblock;

import com.nmmoc7.polymercore.api.component.IMultiblockComponent;
import com.nmmoc7.polymercore.api.machine.IMachine;
import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.IMultiblockType;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.util.PositionUtils;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class DefinedMultiblockImpl extends AbstractMultiblock implements IDefinedMultiblock {
    private ResourceLocation registryName = null;
    private final Map<Vector3i, IMultiblockPart> partsMap;
    private final IMultiblockType type;
    private final boolean canSymmetrical;

    public DefinedMultiblockImpl(List<IMultiblockComponent> components, IMachine machine, Vector3i size, Map<Vector3i, IMultiblockPart> partsMap, IMultiblockType type, boolean canSymmetrical) {
        super(components, machine, size);
        this.partsMap = partsMap;
        this.type = type;
        this.canSymmetrical = canSymmetrical;
    }


    @Nullable
    @Override
    public IAssembledMultiblock assemble(@NotNull World world, @NotNull BlockPos corePos, @NotNull Rotation rotation, boolean isSymmetrical) {
        if (!canAssemble(world, corePos, rotation, isSymmetrical)) {
            return null;
        }
        return getType().createMultiblockIn(this, world, corePos, rotation, isSymmetrical);
    }

    @Nullable
    @Override
    public IAssembledMultiblock assemble(@NotNull World world, @NotNull BlockPos corePos, @NotNull Rotation rotation) {
        if (canAssemble(world, corePos, rotation, true)) {
            return assemble(world, corePos, rotation, true);
        }
        return assemble(world, corePos, rotation, false);
    }

    @Nullable
    @Override
    public IAssembledMultiblock assemble(@NotNull World world, @NotNull BlockPos corePos) {
        for (Rotation value : Rotation.values()) {
            boolean result = canAssemble(world, corePos, value);
            if (result) {
                return assemble(world, corePos, value);
            }
        }
        return null;
    }

    @Override
    public boolean canAssemble(@NotNull World world, @NotNull BlockPos corePos, @NotNull Rotation rotation, boolean isSymmetrical) {
        if (!canSymmetrical && isSymmetrical) {
            return false;
        }
        Map<Vector3i, IMultiblockPart> parts = getParts();
        for (Map.Entry<Vector3i, IMultiblockPart> entry : parts.entrySet()) {
            BlockPos testPos = PositionUtils.applyModifies(entry.getKey(), corePos, rotation, isSymmetrical);
            BlockState block = world.getBlockState(testPos);
            if (!entry.getValue().test(block)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canAssemble(@NotNull World world, @NotNull BlockPos corePos, @NotNull Rotation rotation) {
        return canAssemble(world, corePos, rotation, true) || canAssemble(world, corePos, rotation, false);
    }

    @Override
    public boolean canAssemble(@NotNull World world, @NotNull BlockPos corePos) {
        for (Rotation value : Rotation.values()) {
            boolean result = canAssemble(world, corePos, value);
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
    public Class<IDefinedMultiblock> getRegistryType() {
        return IDefinedMultiblock.class;
    }
}
