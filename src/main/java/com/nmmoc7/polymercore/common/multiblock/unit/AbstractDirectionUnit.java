package com.nmmoc7.polymercore.common.multiblock.unit;

import com.nmmoc7.polymercore.api.multiblock.MultiblockDirection;
import com.nmmoc7.polymercore.api.multiblock.part.IDirectionUnit;
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
