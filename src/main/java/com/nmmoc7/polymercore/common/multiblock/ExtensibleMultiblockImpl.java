package com.nmmoc7.polymercore.common.multiblock;

import com.nmmoc7.polymercore.api.component.IMultiblockComponent;
import com.nmmoc7.polymercore.api.machine.IMachine;
import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import com.nmmoc7.polymercore.api.multiblock.IMultiblockType;
import com.nmmoc7.polymercore.api.multiblock.extension.IExtensibleMultiblock;
import com.nmmoc7.polymercore.api.multiblock.extension.IMultiblockExtension;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ExtensibleMultiblockImpl extends DefinedMultiblockImpl implements IExtensibleMultiblock {
    private final List<IMultiblockExtension> extensions;

    public ExtensibleMultiblockImpl(List<IMultiblockComponent> components, IMachine machine, Vector3i size, Map<Vector3i, IMultiblockPart> partsMap, IMultiblockType type, boolean canSymmetrical, List<IMultiblockExtension> extensions) {
        super(components, machine, size, partsMap, type, canSymmetrical);
        this.extensions = extensions;
    }

    @Override
    public Collection<IMultiblockExtension> getExtensions() {
        return extensions;
    }

    @Nullable
    @Override
    public IAssembledMultiblock assemble(World world, BlockPos corePos, Rotation rotation, boolean isSymmetrical) {
        if (!canAssemble(world, corePos, rotation, isSymmetrical)) {
            return null;
        }
        List<Tuple<IMultiblockExtension, Integer>> extensions = findExtensionsFor(world, corePos, rotation, isSymmetrical);
        return getType().createMultiblockIn(this, world, corePos, rotation, isSymmetrical, extensions);
    }


    @Override
    public List<Tuple<IMultiblockExtension, Integer>> findExtensionsFor(World world, BlockPos pos, Rotation rotation, boolean isSymmetrical) {
        return null;
    }
}
