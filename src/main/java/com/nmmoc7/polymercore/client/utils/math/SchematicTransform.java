package com.nmmoc7.polymercore.client.utils.math;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static net.minecraft.util.math.MathHelper.*;


public class SchematicTransform {
    public SchematicTransform() {
        this.x = 0f;
        this.y = 0f;
        this.z = 0f;
        this.rotation = 0f;
        this.flip = 1f;
    }

    public SchematicTransform(float x, float y, float z, float rotation, float flip) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotation = rotation;
        this.flip = flip;
    }

    public static SchematicTransform create(@NotNull BlockPos offset, @NotNull Rotation rotation, @NotNull boolean isSymmetrical) {
        float rotValue = 360 - rotation.ordinal() * 90;
        float flipValue = isSymmetrical ? -1f : 1f;
        return new SchematicTransform(
            offset.getX(),
            offset.getY(),
            offset.getZ(),
            rotValue,
            flipValue
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

    public void apply(MatrixStack ms) {
        ms.translate(x, y, z);

        ms.translate(0.5f, 0.5f, 0.5f);
        ms.rotate(Vector3f.YP.rotationDegrees(rotation));
        ms.scale(MathHelper.abs(this.flip), 1f, 1f);
        ms.translate(-0.5f, -0.5f, -0.5f);

    }

    public int getFlip() {
        return this.flip > 0 ? 1 : -1;
    }

    public boolean similarTo(SchematicTransform another) {
        if (another == null) {
            return false;
        }
        //noinspection SuspiciousNameCombination
        return epsilonEquals(x, another.x) &&
            epsilonEquals(y, another.y) &&
            epsilonEquals(z, another.z) &&
            epsilonEquals(wrapDegrees(rotation), wrapDegrees(another.rotation)) &&
            epsilonEquals(flip, another.flip);
    }

    public static SchematicTransform interpolate(float percent, SchematicTransform start, SchematicTransform end) {
        return new SchematicTransform(
            lerp(percent, start.x, end.x),
            lerp(percent, start.y, end.y),
            lerp(percent, start.z, end.z),
            interpolateAngle(percent, start.rotation, end.rotation),
            lerp(percent, start.flip, end.flip)
        );
    }

    public SchematicTransform interpolateTo(float percent, SchematicTransform start, SchematicTransform target) {
        x = lerp(percent, start.x, target.x);
        y = lerp(percent, start.y, target.y);
        z = lerp(percent, start.z, target.z);
        rotation = interpolateAngle(percent, start.rotation, target.rotation);
        flip = lerp(percent, start.flip, target.flip);
        return this;
    }

    public SchematicTransform copy() {
        return new SchematicTransform(x, y, z, rotation, flip);
    }
}
