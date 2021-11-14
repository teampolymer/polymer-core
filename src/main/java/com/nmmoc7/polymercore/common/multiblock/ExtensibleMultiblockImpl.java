package com.nmmoc7.polymercore.common.multiblock;

import com.nmmoc7.polymercore.api.component.IMultiblockComponent;
import com.nmmoc7.polymercore.api.machine.IMachine;
import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import com.nmmoc7.polymercore.api.multiblock.IMultiblockType;
import com.nmmoc7.polymercore.api.multiblock.assembled.IMultiblockAssembleRule;
import com.nmmoc7.polymercore.api.multiblock.extension.IExtensibleMultiblock;
import com.nmmoc7.polymercore.api.multiblock.extension.IMultiblockExtension;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.multiblock.part.IPartLimitConfig;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ExtensibleMultiblockImpl extends DefinedMultiblockImpl implements IExtensibleMultiblock {
    private final List<IMultiblockExtension> extensions;

    public ExtensibleMultiblockImpl(List<IMultiblockComponent> components, String machine, Vector3i size, Map<Vector3i, IMultiblockPart> partsMap, IMultiblockType type, boolean canSymmetrical, List<IMultiblockExtension> extensions, List<String> tags, Collection<IPartLimitConfig> limitConfigs) {
        super(components, machine, size, partsMap, type, canSymmetrical, tags, limitConfigs);
        this.extensions = extensions;
    }

    @Override
    public Collection<IMultiblockExtension> getExtensions() {
        return extensions;
    }

    @Nullable
    @Override
    public IAssembledMultiblock assemble(@NotNull World world, @NotNull BlockPos coreOffset, @NotNull Rotation rotation, boolean isSymmetrical) {
        IMultiblockAssembleRule rule = getType().createEmptyRule(coreOffset, rotation, isSymmetrical);
        if (!canAssemble(world, coreOffset, rotation, isSymmetrical)) {
            return null;
        }
        List<Tuple<IMultiblockExtension, Integer>> extensions = findExtensionsFor(world, coreOffset, rotation, isSymmetrical);
        return getType().createMultiblockIn(this, world, rule);
    }


    @Override
    public List<Tuple<IMultiblockExtension, Integer>> findExtensionsFor(World world, BlockPos pos, Rotation rotation, boolean isSymmetrical) {
        return null;
    }
}
