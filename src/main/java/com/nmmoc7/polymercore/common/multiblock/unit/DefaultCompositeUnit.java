package com.nmmoc7.polymercore.common.multiblock.unit;

import com.nmmoc7.polymercore.api.multiblock.MultiblockDirection;
import com.nmmoc7.polymercore.api.multiblock.part.ICompositeUnit;
import com.nmmoc7.polymercore.api.multiblock.part.IDirectionUnit;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
import net.minecraft.block.BlockState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultCompositeUnit implements ICompositeUnit, IDirectionUnit {
    private final List<IMultiblockUnit> childUnits;
    private final List<BlockState> sampleBlocks;
    private final MultiblockDirection direction;

    public DefaultCompositeUnit(IMultiblockUnit... units) {
        if (units.length <= 1) {
            throw new IllegalArgumentException("Cannot composite less than 2 units");
        }

        ArrayList<IMultiblockUnit> childUnits = new ArrayList<>();
        ArrayList<BlockState> sampleBlocks = new ArrayList<>();
        MultiblockDirection direction = null;

        for (IMultiblockUnit unit : units) {
            if (unit instanceof ICompositeUnit) {
                childUnits.addAll(((ICompositeUnit) unit).getChildUnits());
                if (((ICompositeUnit) unit).isDirectionAware()) {
                    direction = ((IDirectionUnit) unit).getDirection();
                }
            } else {
                childUnits.add(unit);
                if (unit instanceof IDirectionUnit) {
                    direction = ((IDirectionUnit) unit).getDirection();
                }
            }

            sampleBlocks.addAll(unit.getSampleBlocks());
        }

        this.childUnits = Collections.unmodifiableList(childUnits);
        this.sampleBlocks = Collections.unmodifiableList(sampleBlocks);

        this.direction = direction;
    }

    @Override
    public List<IMultiblockUnit> getChildUnits() {
        return childUnits;
    }

    @Override
    public boolean isDirectionAware() {
        return direction != null;
    }

    @Override
    public BlockState getSampleBlock() {
        return sampleBlocks.get(0);
    }

    @Override
    public List<BlockState> getSampleBlocks() {
        return sampleBlocks;
    }

    @Override
    public MultiblockDirection getDirection() {
        if (this.direction == null) {
            return MultiblockDirection.NONE;
        }
        return direction;
    }

    @Override
    public IDirectionUnit withDirection(MultiblockDirection direction) {
        if (this.direction == null || this.direction == direction) {
            return this;
        }
        return new DefaultCompositeUnit(
            childUnits.stream()
                .map(it -> it.withDirection(direction))
                .toArray(IMultiblockUnit[]::new)
        );
    }

    @Override
    public boolean test(BlockState block) {
        for (IMultiblockUnit childUnit : childUnits) {
            if (childUnit.test(block)) {
                return true;
            }
        }
        return false;
    }

}
