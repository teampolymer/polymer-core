package com.teampolymer.polymer.core.client.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teampolymer.polymer.core.client.renderer.CustomRenderTypes;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.client.model.data.EmptyModelData;

public class RenderUtils {


    public static void renderBlock(BlockState blockStateIn,
                                   IRenderTypeBuffer bufferSource,
                                   MatrixStack ms,
                                   int combinedLightIn,
                                   GhostBlockUtils.GhostRenderType type) {

        Minecraft mc = Minecraft.getInstance();
        BlockRendererDispatcher dispatcher = mc.getBlockRenderer();
        if (blockStateIn.getRenderShape() == BlockRenderType.MODEL) {
            IBakedModel ibakedmodel = dispatcher.getBlockModel(blockStateIn);
            int i = mc.getBlockColors().getColor(blockStateIn, null, null, 0);
            float r = (float) (i >> 16 & 255) / 255.0F;
            float g = (float) (i >> 8 & 255) / 255.0F;
            float b = (float) (i & 255) / 255.0F;

            IVertexBuilder buffer;
            if (type != GhostBlockUtils.GhostRenderType.ERROR) {
                buffer = bufferSource.getBuffer(CustomRenderTypes.SCHEMATIC_TRANSPARENT);
            } else {
                buffer = bufferSource.getBuffer(CustomRenderTypes.SCHEMATIC_TRANSPARENT_NO_DEPTH);
            }
            buffer = GhostBlockUtils.wrap(buffer, type);
            dispatcher.getModelRenderer().renderModel(
                ms.last(),
                buffer,
                blockStateIn,
                ibakedmodel,
                r, g, b,
                combinedLightIn,
                OverlayTexture.NO_OVERLAY,
                EmptyModelData.INSTANCE);


        } else if (blockStateIn.getRenderShape() == BlockRenderType.ENTITYBLOCK_ANIMATED) {
            GhostBlockUtils.renderGhostBlock(blockStateIn, bufferSource, ms, combinedLightIn, type);

        } else {
            IVertexBuilder buffer;
            if (type != GhostBlockUtils.GhostRenderType.ERROR) {
                buffer = bufferSource.getBuffer(CustomRenderTypes.SCHEMATIC_TRANSPARENT);
            } else {
                buffer = bufferSource.getBuffer(CustomRenderTypes.SCHEMATIC_TRANSPARENT_NO_DEPTH);
            }
            buffer = GhostBlockUtils.wrap(buffer, type);
            FluidState fluidState = blockStateIn.getFluidState();
            if (!fluidState.isEmpty()) {
                FluidRenderUtils.renderFluid(buffer, fluidState, ms, combinedLightIn, OverlayTexture.NO_OVERLAY);
            }
        }
    }


    @SuppressWarnings("DuplicatedCode")
    public static void renderCube(MatrixStack ms, IVertexBuilder builder, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float red, float green, float blue, float alpha) {
        Matrix4f mat = ms.last().pose();

        builder.vertex(mat, maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(mat, minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(mat, minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(mat, maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();

        builder.vertex(mat, maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(mat, minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(mat, minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(mat, maxX, minY, minZ).color(red, green, blue, alpha).endVertex();

        builder.vertex(mat, maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(mat, minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(mat, minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(mat, maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();

        builder.vertex(mat, maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(mat, minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(mat, minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(mat, maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();

        builder.vertex(mat, minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(mat, minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(mat, minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(mat, minX, minY, maxZ).color(red, green, blue, alpha).endVertex();

        builder.vertex(mat, maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(mat, maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(mat, maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(mat, maxX, minY, minZ).color(red, green, blue, alpha).endVertex();

    }

}
