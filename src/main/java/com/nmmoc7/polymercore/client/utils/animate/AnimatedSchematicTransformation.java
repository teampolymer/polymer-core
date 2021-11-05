package com.nmmoc7.polymercore.client.utils.animate;

import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.jozufozu.flywheel.util.transform.MatrixTransformStack;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

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
        float pt = AnimationTickHolder.getPartialTicks();

        // Translation
        ms.translate(x.get(pt), y.get(pt), z.get(pt));
        Vector3d rotationOffset = new Vector3d(0.5, 0.5, 0.5);

        // Rotation
        float rot = rotation.get(pt);
        MatrixTransformStack.of(ms)
            .translate(rotationOffset)
            .rotateY(rot)
            .translateBack(rotationOffset);

        //Mirror
        float flip = flipScale.get(pt);
        ms.scale(MathHelper.abs(flip), 1, 1);
    }


}
