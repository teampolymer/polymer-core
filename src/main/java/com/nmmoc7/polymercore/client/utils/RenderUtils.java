package com.nmmoc7.polymercore.client.utils;

import com.jozufozu.flywheel.util.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.nmmoc7.polymercore.client.renderer.SchematicRenderTypes;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;

public class RenderUtils {


    public static void renderBlock(BlockState blockStateIn,
                                   IRenderTypeBuffer bufferSource,
                                   MatrixStack matrixStackIn,
                                   int combinedLightIn,
                                   IModelData modelData) {
        if (blockStateIn.getRenderType() == BlockRenderType.MODEL) {
            Minecraft mc = Minecraft.getInstance();
            BlockRendererDispatcher dispatcher = mc.getBlockRendererDispatcher();
            IBakedModel ibakedmodel = dispatcher.getModelForState(blockStateIn);
            int i = Minecraft.getInstance().getBlockColors().getColor(blockStateIn, null, null, 0);
            float f = (float) (i >> 16 & 255) / 255.0F;
            float f1 = (float) (i >> 8 & 255) / 255.0F;
            float f2 = (float) (i & 255) / 255.0F;
            IVertexBuilder buffer = bufferSource.getBuffer(SchematicRenderTypes.TRANSPARENT_BLOCK);
            dispatcher.getBlockModelRenderer().renderModel(
                matrixStackIn.getLast(),
                buffer,
                blockStateIn,
                ibakedmodel,
                f, f1, f2,
                combinedLightIn,
                OverlayTexture.NO_OVERLAY,
                modelData != null ? modelData : EmptyModelData.INSTANCE);

        }
    }


}
