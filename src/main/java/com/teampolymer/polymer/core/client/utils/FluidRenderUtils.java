package com.teampolymer.polymer.core.client.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teampolymer.polymer.core.client.utils.math.RenderMath;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Predicate;

@SuppressWarnings("DuplicatedCode")
public class FluidRenderUtils {


    public static void renderFluid(IVertexBuilder builder, FluidState fluidState, MatrixStack ms, int combinedLight, int combinedOverlay) {

        if (fluidState.isEmpty()) return;
        Fluid fluid = fluidState.getType();
        float height = fluid.getOwnHeight(fluidState);

        renderFluid(builder, fluid, new AxisAlignedBB(0, 0, 0, 1, height, 1), dir -> true, ms, combinedLight, combinedOverlay);

    }

    public static void renderFluid(IVertexBuilder builder, Fluid fluid, AxisAlignedBB bounds, Predicate<Direction> directionPredicate, MatrixStack ms, int combinedLight, int combinedOverlay) {
        ResourceLocation texture = fluid.getAttributes().getStillTexture();
        TextureAtlasSprite still = Minecraft.getInstance().getTextureAtlas(PlayerContainer.BLOCK_ATLAS).apply(texture);
        int[] cols = RenderMath.decomposeColor(fluid.getAttributes().getColor());

        float x1 = (float) bounds.minX;
        float x2 = (float) bounds.maxX;
        float y1 = (float) bounds.minY;
        float y2 = (float) bounds.maxY;
        float z1 = (float) bounds.minZ;
        float z2 = (float) bounds.maxZ;
        double bx1 = bounds.minX * 16;
        double bx2 = bounds.maxX * 16;
        double by1 = bounds.minY * 16;
        double by2 = bounds.maxY * 16;
        double bz1 = bounds.minZ * 16;
        double bz2 = bounds.maxZ * 16;

        Matrix4f pose = ms.last().pose();
        Matrix3f normal = ms.last().normal();

        if (directionPredicate.test(Direction.DOWN)) {
            float u1 = still.getU(bx1);
            float u2 = still.getU(bx2);
            float v1 = still.getV(bz1);
            float v2 = still.getV(bz2);
            builder.vertex(pose, x1, y1, z2).color(cols[1], cols[2], cols[3], cols[0]).uv(u1, v2).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, 0f, -1f, 0f).endVertex();
            builder.vertex(pose, x1, y1, z1).color(cols[1], cols[2], cols[3], cols[0]).uv(u1, v1).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, 0f, -1f, 0f).endVertex();
            builder.vertex(pose, x2, y1, z1).color(cols[1], cols[2], cols[3], cols[0]).uv(u2, v1).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, 0f, -1f, 0f).endVertex();
            builder.vertex(pose, x2, y1, z2).color(cols[1], cols[2], cols[3], cols[0]).uv(u2, v2).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, 0f, -1f, 0f).endVertex();
        }

        if (directionPredicate.test(Direction.UP)) {
            float u1 = still.getU(bx1);
            float u2 = still.getU(bx2);
            float v1 = still.getV(bz1);
            float v2 = still.getV(bz2);
            builder.vertex(pose, x1, y2, z2).color(cols[1], cols[2], cols[3], cols[0]).uv(u1, v2).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, 0f, 1f, 0f).endVertex();
            builder.vertex(pose, x2, y2, z2).color(cols[1], cols[2], cols[3], cols[0]).uv(u2, v2).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, 0f, 1f, 0f).endVertex();
            builder.vertex(pose, x2, y2, z1).color(cols[1], cols[2], cols[3], cols[0]).uv(u2, v1).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, 0f, 1f, 0f).endVertex();
            builder.vertex(pose, x1, y2, z1).color(cols[1], cols[2], cols[3], cols[0]).uv(u1, v1).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, 0f, 1f, 0f).endVertex();
        }

        if (directionPredicate.test(Direction.NORTH)) {
            float u1 = still.getU(bx1);
            float u2 = still.getU(bx2);
            float v1 = still.getV(by1);
            float v2 = still.getV(by2);
            builder.vertex(pose, x1, y1, z1).color(cols[1], cols[2], cols[3], cols[0]).uv(u1, v1).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, 0f, 0f, -1f).endVertex();
            builder.vertex(pose, x1, y2, z1).color(cols[1], cols[2], cols[3], cols[0]).uv(u1, v2).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, 0f, 0f, -1f).endVertex();
            builder.vertex(pose, x2, y2, z1).color(cols[1], cols[2], cols[3], cols[0]).uv(u2, v2).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, 0f, 0f, -1f).endVertex();
            builder.vertex(pose, x2, y1, z1).color(cols[1], cols[2], cols[3], cols[0]).uv(u2, v1).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, 0f, 0f, -1f).endVertex();
        }

        if (directionPredicate.test(Direction.SOUTH)) {
            float u1 = still.getU(bx1);
            float u2 = still.getU(bx2);
            float v1 = still.getV(by1);
            float v2 = still.getV(by2);
            builder.vertex(pose, x2, y1, z2).color(cols[1], cols[2], cols[3], cols[0]).uv(u2, v1).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, 0f, 0f, 1f).endVertex();
            builder.vertex(pose, x2, y2, z2).color(cols[1], cols[2], cols[3], cols[0]).uv(u2, v2).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, 0f, 0f, 1f).endVertex();
            builder.vertex(pose, x1, y2, z2).color(cols[1], cols[2], cols[3], cols[0]).uv(u1, v2).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, 0f, 0f, 1f).endVertex();
            builder.vertex(pose, x1, y1, z2).color(cols[1], cols[2], cols[3], cols[0]).uv(u1, v1).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, 0f, 0f, 1f).endVertex();
        }

        if (directionPredicate.test(Direction.WEST)) {
            float u1 = still.getU(by1);
            float u2 = still.getU(by2);
            float v1 = still.getV(bz1);
            float v2 = still.getV(bz2);
            builder.vertex(pose, x1, y1, z2).color(cols[1], cols[2], cols[3], cols[0]).uv(u1, v2).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, -1f, 0f, 0f).endVertex();
            builder.vertex(pose, x1, y2, z2).color(cols[1], cols[2], cols[3], cols[0]).uv(u2, v2).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, -1f, 0f, 0f).endVertex();
            builder.vertex(pose, x1, y2, z1).color(cols[1], cols[2], cols[3], cols[0]).uv(u2, v1).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, -1f, 0f, 0f).endVertex();
            builder.vertex(pose, x1, y1, z1).color(cols[1], cols[2], cols[3], cols[0]).uv(u1, v1).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, -1f, 0f, 0f).endVertex();
        }

        if (directionPredicate.test(Direction.EAST)) {
            float u1 = still.getU(by1);
            float u2 = still.getU(by2);
            float v1 = still.getV(bz1);
            float v2 = still.getV(bz2);
            builder.vertex(pose, x2, y1, z1).color(cols[1], cols[2], cols[3], cols[0]).uv(u1, v1).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, 1f, 0f, 0f).endVertex();
            builder.vertex(pose, x2, y2, z1).color(cols[1], cols[2], cols[3], cols[0]).uv(u2, v1).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, 1f, 0f, 0f).endVertex();
            builder.vertex(pose, x2, y2, z2).color(cols[1], cols[2], cols[3], cols[0]).uv(u2, v2).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, 1f, 0f, 0f).endVertex();
            builder.vertex(pose, x2, y1, z2).color(cols[1], cols[2], cols[3], cols[0]).uv(u1, v2).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normal, 1f, 0f, 0f).endVertex();
        }
    }
}
