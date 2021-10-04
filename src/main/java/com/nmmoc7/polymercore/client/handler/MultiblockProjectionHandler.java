package com.nmmoc7.polymercore.client.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.nmmoc7.polymercore.PolymerCore;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.util.PositionUtils;
import com.nmmoc7.polymercore.client.renderer.ProjectionRenderType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = PolymerCore.MOD_ID)
@OnlyIn(Dist.CLIENT)
public class MultiblockProjectionHandler {
    private static IDefinedMultiblock multiblock;
    private static boolean isProjecting = false;
    private static BlockPos targetPos;
    private static Rotation rotation = Rotation.NONE;
    private static boolean isSymmetrical = false;

    public static void setMultiblock(IDefinedMultiblock multiblock) {
        MultiblockProjectionHandler.multiblock = multiblock;
    }

    public static void setIsProjecting(boolean isProjecting) {
        MultiblockProjectionHandler.isProjecting = isProjecting;
    }

    public static void setTargetPos(BlockPos targetPos) {
        MultiblockProjectionHandler.targetPos = targetPos;
    }

    public static void setRotation(Rotation rotation) {
        MultiblockProjectionHandler.rotation = rotation;
    }

    public static void setIsSymmetrical(boolean isSymmetrical) {
        MultiblockProjectionHandler.isSymmetrical = isSymmetrical;
    }

    @SubscribeEvent
    public static void onWorldRenderLast(RenderWorldLastEvent event) {
        if (isProjecting && multiblock != null) {
            renderMultiblock(Minecraft.getInstance().world, event.getContext(), event.getMatrixStack());
        }
    }

    public static void renderMultiblock(World world, WorldRenderer context, MatrixStack ms) {
        BlockPos pos = targetPos;
        //施法者->轴向向量追踪（大雾）
        BlockPos trackPos = null;

        Minecraft mc = Minecraft.getInstance();


        //获取玩家正在看向的方块
        if (mc.objectMouseOver instanceof BlockRayTraceResult) {
            BlockRayTraceResult rayTraceResult = (BlockRayTraceResult) mc.objectMouseOver;
            if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
                if (targetPos == null) {
                    pos = rayTraceResult.getPos();
                } else {
                    trackPos = rayTraceResult.getPos().offset(rayTraceResult.getFace());
                }

            }
        }

        if (pos == null) {
            return;
        }

        ms.push();
        Vector3d view = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
        //坐标向玩家视角偏移
        ms.translate(-view.x, -view.y, -view.z);

        IRenderTypeBuffer.Impl bufferSource = mc.getRenderTypeBuffers().getBufferSource();

        RenderSystem.disableDepthTest();
        for (Map.Entry<Vector3i, IMultiblockPart> entry : multiblock.getParts().entrySet()) {
            Vector3i relative = entry.getKey();
            //TODO: 这里应该循环显示所有可能的方块
            BlockState block = entry.getValue().getFirstMatchingBlock();
            BlockPos offPos = PositionUtils.applyModifies(relative, pos, rotation, isSymmetrical);

            float alpha = 0.3F;
            if (offPos.equals(trackPos)) {
                alpha = 0.7F + (float) (Math.sin(ClientEventHandler.elapsedTicks * 0.2F) + 1F) * 0.2F;
            }

            BlockState current = world.getBlockState(offPos);
            if (current != block) {
                ms.push();
                ms.translate(offPos.getX(), offPos.getY(), offPos.getZ());

                IModelData modelData = ModelDataManager.getModelData(world, offPos);

//                int blockLight = world.getLightFor(LightType.BLOCK, pos);
//                int sky = world.getLightFor(LightType.SKY, pos);
//                int combinedLight = LightTexture.packLight(Math.max(15, blockLight + 8), sky);
                //0xF000F0就是天空亮度15方块亮度15
                RenderSystem.blendColor(1, 1, 1, alpha);
                renderBlock(block, bufferSource, ms, 0xF000F0, modelData);
                bufferSource.finish(ProjectionRenderType.TRANSPARENT_BLOCK);
                RenderSystem.blendColor(1, 1, 1, 1);
                ms.pop();
            }
        }
        RenderSystem.enableDepthTest();
        ms.pop();

    }


    private static void renderBlock(BlockState blockStateIn,
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
            IVertexBuilder buffer = bufferSource.getBuffer(ProjectionRenderType.TRANSPARENT_BLOCK);
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
