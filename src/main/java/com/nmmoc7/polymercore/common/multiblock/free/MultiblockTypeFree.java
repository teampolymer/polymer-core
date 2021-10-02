package com.nmmoc7.polymercore.common.multiblock.free;

import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.IMultiblockType;
import com.nmmoc7.polymercore.api.multiblock.extension.IMultiblockExtension;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class MultiblockTypeFree implements IMultiblockType {
    @Override
    public IAssembledMultiblock createMultiblockIn(IDefinedMultiblock definition, World world, BlockPos pos, Rotation rotation, boolean isSymmetrical, List<Tuple<IMultiblockExtension, Integer>> appliedExtensions) {
        return null;
    }

    @Override
    public IAssembledMultiblock createMultiblockIn(IDefinedMultiblock definition, World world, BlockPos pos, Rotation rotation, boolean isSymmetrical) {
        return null;
    }
}
