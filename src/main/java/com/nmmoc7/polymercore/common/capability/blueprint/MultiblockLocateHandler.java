package com.nmmoc7.polymercore.common.capability.blueprint;

import com.nmmoc7.polymercore.api.capability.IMultiblockLocateHandler;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

public class MultiblockLocateHandler implements IMultiblockLocateHandler {
    private BlockPos offset;
    private Rotation rotation;
    private boolean flipped;
    private boolean anchored;

    @Override
    public boolean isAnchored() {
        return anchored;
    }

    @Override
    public void setAnchored(boolean anchored) {
        this.anchored = anchored;
    }


    @Override
    public BlockPos getOffset() {
        return offset;
    }

    @Override
    public void setOffset(BlockPos offset) {
        this.offset = offset;
    }

    @Override
    public Rotation getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    @Override
    public boolean isFlipped() {
        return flipped;
    }

    @Override
    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }
}
