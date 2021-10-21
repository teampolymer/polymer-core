package com.nmmoc7.polymercore.client.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.util.PositionUtils;
import com.nmmoc7.polymercore.client.renderer.ProjectionRenderType;
import com.nmmoc7.polymercore.client.utils.AnimationTickHelper;
import com.nmmoc7.polymercore.client.utils.RenderUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = PolymerCoreApi.MOD_ID)
@OnlyIn(Dist.CLIENT)
public class MultiblockProjectionHandler {
    private static IDefinedMultiblock multiblock;
    private static boolean isProjecting = false;
    private static BlockPos targetPos;
    private static Rotation rotation = Rotation.NONE;
    private static boolean isSymmetrical = false;
    private static IVertexBuilder buffer;

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
        if (mc.player == null) {
            return;
        }

        float renderTimes = AnimationTickHelper.getRenderTime();
        int renderTicks = AnimationTickHelper.getTicks();

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

        for (Map.Entry<Vector3i, IMultiblockPart> entry : multiblock.getParts().entrySet()) {
            Vector3i relative = entry.getKey();
            List<BlockState> sampleBlocks = entry.getValue().getSampleBlocks();
            int i = (renderTicks) / 20 % sampleBlocks.size();

            BlockState block = sampleBlocks.get(i);
            BlockPos offPos = PositionUtils.applyModifies(relative, pos, rotation, isSymmetrical);


            BlockState current = world.getBlockState(offPos);
            ms.push();
            ms.translate(offPos.getX(), offPos.getY(), offPos.getZ());
            if (!entry.getValue().test(current)) {
                IModelData modelData = ModelDataManager.getModelData(world, offPos);

//                int blockLight = world.getLightFor(LightType.BLOCK, pos);
//                int sky = world.getLightFor(LightType.SKY, pos);
//                int combinedLight = LightTexture.packLight(Math.max(15, blockLight + 8), sky);
                //0xF000F0就是天空亮度15方块亮度15

                if (current.getBlock() != Blocks.AIR) {
                    //稍微放大一丁点，防止贴图错误
                    ms.scale(1.002f, 1.002f, 1.002f);
                    ms.translate(-0.001f, -0.001f, -0.001f);
                    //如果不对，显示红色边框，潜行的时候透视
                    IVertexBuilder buffer = mc.player.isSneaking() ? bufferSource.getBuffer(ProjectionRenderType.LINE_NO_DEPTH) : bufferSource.getBuffer(RenderType.LINES);
                    WorldRenderer.drawBoundingBox(ms, buffer,
                        0, 0, 0, 1, 1, 1
                        , 1.0f, 0.1f, 0.07f, 0.8f);

                } else {
                    //正常情况下稍微缩小一点点
                    ms.scale(0.94f, 0.94f, 0.94f);
                    ms.translate(0.03f, 0.03f, 0.03f);
                }
                //默认透明度0.3
                float alpha = 0.3F;
                //对当前选中的方块提高透明度
                if (offPos.equals(trackPos)) {
                    alpha = 0.6F + (float) (Math.sin(renderTimes * 0.2F) + 1F) * 0.15F;
                }
                //渲染投影
                RenderSystem.blendColor(1, 1, 1, alpha);
                RenderUtils.renderBlock(block, bufferSource, ms, 0xF000F0, modelData);
                bufferSource.finish(ProjectionRenderType.TRANSPARENT_BLOCK);
                RenderSystem.blendColor(1, 1, 1, 1);
            } else {
                //稍微放大一丁点，防止贴图错误
                ms.scale(1.002f, 1.002f, 1.002f);
                ms.translate(-0.001f, -0.001f, -0.001f);
                //正确，显示绿色
                WorldRenderer.drawBoundingBox(ms, bufferSource.getBuffer(RenderType.LINES),
                    0, 0, 0, 1, 1, 1
                    , 0.3f, 1.0f, 0.15f, 0.6f);
            }
            ms.pop();
        }
        bufferSource.finish();
        ms.pop();

    }


}
