package com.nmmoc7.polymercore.common.multiblock.free;

import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
import com.nmmoc7.polymercore.common.multiblock.assembled.AbstractAssembleRule;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public class FreeMultiblockAssembleRule extends AbstractAssembleRule {
    public FreeMultiblockAssembleRule(BlockPos offset, boolean isSymmetrical, Rotation rotation) {
        super(offset, isSymmetrical, rotation);
    }

    public FreeMultiblockAssembleRule() {

    }

    @Override
    public Map<BlockPos, IMultiblockUnit> mapParts(IDefinedMultiblock originalMultiblock) {
        return null;
    }
}
