package com.teampolymer.polymer.core.common.multiblock.unit;

import com.teampolymer.polymer.core.api.multiblock.MultiblockDirection;
import com.teampolymer.polymer.core.api.multiblock.part.IDirectionUnit;
import com.teampolymer.polymer.core.api.multiblock.part.IMultiblockUnit;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.Fluids;

import java.util.List;

public class UnitWaterLogged implements IDirectionUnit {
    private final IMultiblockUnit part;

    public UnitWaterLogged(IMultiblockUnit parent) {
        part = parent;
    }

    @Override
    public BlockState getSampleBlock() {
        return part.getSampleBlock();
    }

    @Override
    public MultiblockDirection getDirection() {
        if (part instanceof IDirectionUnit) {
            return ((IDirectionUnit) part).getDirection();
        }
        return MultiblockDirection.NONE;
    }

    @Override
    public List<BlockState> getSampleBlocks() {
        return part.getSampleBlocks();
    }

    @Override
    public IDirectionUnit withDirection(MultiblockDirection direction) {
        if (direction == getDirection()) {
            return this;
        }
        if (part instanceof IDirectionUnit) {
            IDirectionUnit result = ((IDirectionUnit) part).withDirection(direction);
            if (result != part) {
                return new UnitWaterLogged(part);
            }
        }
        return this;
    }

    @Override
    public boolean test(BlockState block) {
        if (!(block instanceof IWaterLoggable)) {
            return false;
        }
        if (!part.test(block)) {
            return false;
        }
        return block.getFluidState().getType() == Fluids.WATER;

    }

}
