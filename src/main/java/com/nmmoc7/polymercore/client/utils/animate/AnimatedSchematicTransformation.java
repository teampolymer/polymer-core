package com.nmmoc7.polymercore.client.utils.animate;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nmmoc7.polymercore.client.utils.AnimationTickHelper;
import net.minecraft.util.math.BlockPos;

public class AnimatedSchematicTransformation {
    private AnimatedValue x;
    private AnimatedValue y;
    private AnimatedValue z;
    private AnimatedValue rotation;
    private AnimatedValue flipScale;

    public void tick() {
        x.tick();
        y.tick();
        z.tick();
        flipScale.tick();
        rotation.tick();
    }

    public void moveTo(BlockPos pos) {
        moveTo(pos.getX(), pos.getY(), pos.getZ());
    }

    public void moveTo(float xIn, float yIn, float zIn) {
        x.target(xIn);
        y.target(yIn);
        z.target(zIn);
    }

    public void rotate90(boolean clockwise) {
        rotation.target(rotation.getTarget() + (clockwise ? -90 : 90));
    }

    public void flip() {
        flipScale.target(flipScale.getTarget() * -1);
    }


    public boolean settled() {
        return x.settled() &&
            y.settled() &&
            z.settled() &&
            flipScale.settled() &&
            rotation.settled();
    }

    public void applyToMS(MatrixStack ms) {

    }


}
