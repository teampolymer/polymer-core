package com.teampolymer.polymer.core.common.multiblock.unit;

import com.teampolymer.polymer.core.api.multiblock.MultiblockDirection;
import com.teampolymer.polymer.core.api.multiblock.part.IDirectionUnit;
import net.minecraft.block.BlockState;

import java.util.List;

public abstract class AbstractDirectionUnit extends AbstractUnit implements IDirectionUnit {
    private final MultiblockDirection direction;

    public AbstractDirectionUnit(List<BlockState> samples, MultiblockDirection direction) {
        super(samples);
        this.direction = direction;
    }

    @Override
    public IDirectionUnit withDirection(MultiblockDirection direction) {
        return this;
    }

    @Override
    public MultiblockDirection getDirection() {
        return direction;
    }
}
