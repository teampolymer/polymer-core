package com.nmmoc7.polymercore.client.utils.math;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.util.math.MathHelper.*;


public class SchematicTransform {
    public SchematicTransform() {
        this.x = 0f;
        this.y = 0f;
        this.z = 0f;
        this.rotation = 0f;
        this.flip = 1f;
        this.scale = 0f;
    }

    public SchematicTransform(float x, float y, float z, float rotation, float flip, float scale) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotation = rotation;
        this.flip = flip;
        this.scale = scale;
    }

    public static SchematicTransform create(BlockPos offset, @NotNull Rotation rotation, @NotNull boolean isSymmetrical) {
        if (offset == null) {
            offset = BlockPos.ZERO;
        }
        float rotValue = 360 - rotation.ordinal() * 90;
        float flipValue = isSymmetrical ? -1f : 1f;
        return new SchematicTransform(
            offset.getX(),
            offset.getY(),
            offset.getZ(),
            rotValue,
            flipValue,
            1.0f
        );
    }

    //移动坐标
    public float x;
    public float y;
    public float z;

    //旋转角度 [范围0-360)
    public float rotation;

    //对称反转程度 [-1,1]
    public float flip;

    public float scale;

    public void applyEntirely(MatrixStack ms) {
        ms.translate(x, y, z);

        ms.translate(0.5f, 0.5f, 0.5f);
        ms.mulPose(Vector3f.YP.rotationDegrees(rotation));
        ms.scale(MathHelper.abs(this.flip), 1f, 1f);
        ms.translate(-0.5f, -0.5f, -0.5f);

    }

    public void applyPartially(MatrixStack ms) {
        float scale = MathHelper.abs(this.scale);
        float offset = (1f - scale) / 2;
        ms.translate(offset, offset, offset);
        ms.scale(scale, scale, scale);
    }

    public SchematicTransform shrink(float scale) {
        this.scale = scale;
        return this;
    }


    public int getFlip() {
        return this.flip > 0 ? 1 : -1;
    }
    public boolean isFlipped() {
        return this.flip > 0;
    }

    public boolean similarTo(SchematicTransform another) {
        if (another == null) {
            return false;
        }
        //noinspection SuspiciousNameCombination
        return equal(x, another.x) &&
            equal(y, another.y) &&
            equal(z, another.z) &&
            equal(wrapDegrees(rotation), wrapDegrees(another.rotation)) &&
            equal(flip, another.flip);
    }

    public static SchematicTransform interpolate(float percent, SchematicTransform start, SchematicTransform end) {
        return new SchematicTransform(
            lerp(percent, start.x, end.x),
            lerp(percent, start.y, end.y),
            lerp(percent, start.z, end.z),
            rotLerp(percent, start.rotation, end.rotation),
            lerp(percent, start.flip, end.flip),
            lerp(percent, start.scale, end.scale)
        );
    }

    public SchematicTransform interpolateTo(float percent, SchematicTransform start, SchematicTransform target) {
        x = lerp(percent, start.x, target.x);
        y = lerp(percent, start.y, target.y);
        z = lerp(percent, start.z, target.z);
        rotation = rotLerp(percent, start.rotation, target.rotation);
        flip = lerp(percent, start.flip, target.flip);
        scale = lerp(percent, start.scale, target.scale);
        return this;
    }

    public SchematicTransform copy() {
        return new SchematicTransform(x, y, z, rotation, flip, scale);
    }
}
