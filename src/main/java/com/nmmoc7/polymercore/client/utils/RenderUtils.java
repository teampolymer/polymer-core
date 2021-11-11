package com.nmmoc7.polymercore.client.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;

public class RenderUtils {


    public static void renderBlock(BlockState blockStateIn,
                                   IRenderTypeBuffer bufferSource,
                                   MatrixStack ms,
                                   int combinedLightIn,
                                   IModelData modelData,
                                   RenderType renderType) {
        Minecraft mc = Minecraft.getInstance();
        BlockRendererDispatcher dispatcher = mc.getBlockRendererDispatcher();
        FluidState fluidState = blockStateIn.getFluidState();
        if(fluidState.getFluid() !=  Fluids.EMPTY) {

        }
        if (blockStateIn.getRenderType() == BlockRenderType.MODEL) {
            IBakedModel ibakedmodel = dispatcher.getModelForState(blockStateIn);
            int i = mc.getBlockColors().getColor(blockStateIn, null, null, 0);
            float r = (float) (i >> 16 & 255) / 255.0F;
            float g = (float) (i >> 8 & 255) / 255.0F;
            float b = (float) (i & 255) / 255.0F;
            IVertexBuilder buffer = bufferSource.getBuffer(renderType);
            dispatcher.getBlockModelRenderer().renderModel(
                ms.getLast(),
                buffer,
                blockStateIn,
                ibakedmodel,
                r, g, b,
                combinedLightIn,
                OverlayTexture.NO_OVERLAY,
                modelData != null ? modelData : EmptyModelData.INSTANCE);

        }

    }

    @SuppressWarnings("DuplicatedCode")
    public static void renderCube(MatrixStack ms, IVertexBuilder builder, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float red, float green, float blue, float alpha) {
        Matrix4f mat = ms.getLast().getMatrix();

        builder.pos(mat, maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(mat, minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(mat, minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(mat, maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();

        builder.pos(mat, maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(mat, minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(mat, minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(mat, maxX, minY, minZ).color(red, green, blue, alpha).endVertex();

        builder.pos(mat, maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(mat, minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(mat, minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(mat, maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();

        builder.pos(mat, maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(mat, minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(mat, minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(mat, maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();

        builder.pos(mat, minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(mat, minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(mat, minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(mat, minX, minY, maxZ).color(red, green, blue, alpha).endVertex();

        builder.pos(mat, maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(mat, maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(mat, maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(mat, maxX, minY, minZ).color(red, green, blue, alpha).endVertex();

    }

}
