package com.nmmoc7.polymercore.client.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.nmmoc7.polymercore.client.event.SchematicSpecialRenderEvent;
import com.nmmoc7.polymercore.client.renderer.CustomRenderTypes;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.common.MinecraftForge;

public class RenderUtils {


    public static void renderBlock(BlockState blockStateIn,
                                   IRenderTypeBuffer bufferSource,
                                   MatrixStack ms,
                                   int combinedLightIn,
                                   boolean isDynamic) {


        boolean cancelled = MinecraftForge.EVENT_BUS.post(new SchematicSpecialRenderEvent(blockStateIn, bufferSource, ms, isDynamic, combinedLightIn));
        if (cancelled) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        BlockRendererDispatcher dispatcher = mc.getBlockRenderer();
        if (blockStateIn.getRenderShape() == BlockRenderType.MODEL) {
            IBakedModel ibakedmodel = dispatcher.getBlockModel(blockStateIn);
            int i = mc.getBlockColors().getColor(blockStateIn, null, null, 0);
            float r = (float) (i >> 16 & 255) / 255.0F;
            float g = (float) (i >> 8 & 255) / 255.0F;
            float b = (float) (i & 255) / 255.0F;
            IVertexBuilder buffer = bufferSource.getBuffer(isDynamic ? CustomRenderTypes.TRANSPARENT_BLOCK_DYNAMIC : CustomRenderTypes.TRANSPARENT_BLOCK);
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
            GhostBlockUtils.renderGhostBlock(blockStateIn, bufferSource, ms, combinedLightIn, isDynamic);
        } else {
            float alpha = 0.3f;
            if (isDynamic) {
                alpha = AnimationTickHelper.sinCirculateIn(0.5f, 0.8f, 30);
            }

            IFormattableTextComponent text = blockStateIn.getBlock().getName();
            int color = NativeImage.combine((int) (255 * Math.min(alpha + 0.2, 1)), 255, 255, 255);
            float width = mc.font.width(text);

            for (int i = 0; i < 4; i++) {
                ms.pushPose();
                ms.translate(0.5, 0.5, 0.5);
                ms.mulPose(Vector3f.XP.rotationDegrees(180));
                ms.mulPose(Vector3f.YP.rotationDegrees(90 * i));
                ms.translate(-0.5, -0.5, -0.5);

                ms.translate(0.5f, 0.5f, 0.102f);
                ms.scale(0.015F, 0.015F, 0.015F);


                RenderSystem.enableDepthTest();
                IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
                mc.font.drawInBatch(text, -width / 2, -10f, color, false, ms.last().pose(), buffer, true, 0, 15728880);
                buffer.endBatch();
                RenderSystem.disableDepthTest();
                //mc.fontRenderer.renderString("2333", -width / 2, -10f, 0xFFFFFF66, ms.getLast().getMatrix(), false, true);
                ms.popPose();
            }

            if (blockStateIn.getRenderShape() == BlockRenderType.INVISIBLE && !blockStateIn.getFluidState().isEmpty()) {
                int fluidColor = blockStateIn.getFluidState().getType().getAttributes().getColor();
                float r = (float) (fluidColor >> 16 & 255) / 255.0F;
                float g = (float) (fluidColor >> 8 & 255) / 255.0F;
                float b = (float) (fluidColor & 255) / 255.0F;
                renderCube(ms, bufferSource.getBuffer(CustomRenderTypes.CUBE_NORMAL),
                    0.1f, 0.1f, 0.1f,
                    0.9f, 0.9f, 0.9f,
                    r, g, b, alpha);
            } else {
                renderCube(ms, bufferSource.getBuffer(CustomRenderTypes.CUBE_NORMAL),
                    0.1f, 0.1f, 0.1f,
                    0.9f, 0.9f, 0.9f,
                    1f, 0.7f, 0.5f, alpha);
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
