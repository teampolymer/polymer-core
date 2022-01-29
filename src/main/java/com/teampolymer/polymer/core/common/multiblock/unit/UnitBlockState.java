package com.teampolymer.polymer.core.common.multiblock.unit;

import com.teampolymer.polymer.core.api.multiblock.MultiblockDirection;
import com.teampolymer.polymer.core.api.multiblock.part.IDirectionUnit;
import com.teampolymer.polymer.core.api.util.MultiblockPartUtils;
import net.minecraft.block.BlockState;

import java.util.Collections;

public class UnitBlockState extends AbstractDirectionUnit {
    private final BlockState blockState;

    public UnitBlockState(BlockState block) {
        this(block, MultiblockDirection.NONE);
    }

    public UnitBlockState(BlockState block, MultiblockDirection direction) {
        super(Collections.singletonList(block), direction);
        this.blockState = block;
    }


    @Override
    public boolean test(BlockState block) {
        return blockState == block;
    }

    @Override
    public IDirectionUnit withDirection(MultiblockDirection direction) {
        if (direction == this.getDirection()) {
            return this;
        }
        BlockState blockState = MultiblockPartUtils.withDirection(this.blockState, direction);
        if (blockState != this.blockState) {
            return new UnitBlockState(blockState, direction);
        }
        return this;
    }
}
