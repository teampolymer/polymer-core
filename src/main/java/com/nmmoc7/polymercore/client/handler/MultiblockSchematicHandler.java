package com.nmmoc7.polymercore.client.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.util.PositionUtils;
import com.nmmoc7.polymercore.client.renderer.CustomRenderTypeBuffer;
import com.nmmoc7.polymercore.client.renderer.IRenderer;
import com.nmmoc7.polymercore.client.renderer.ProjectionRenderType;
import com.nmmoc7.polymercore.client.utils.AnimationTickHelper;
import com.nmmoc7.polymercore.client.utils.RenderUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * 渲染多方快结构蓝图投影
 */
public class MultiblockSchematicHandler implements IRenderer {
    public static final MultiblockSchematicHandler INSTANCE = new MultiblockSchematicHandler();


    private boolean enabled = false;


    //多方快
    private static IDefinedMultiblock multiblock;
    private static BlockPos targetPos;
    private static BlockPos targetPosPrevious;
    private static Rotation rotation = Rotation.NONE;
    private static boolean isSymmetrical = false;

    @Override
    public boolean isEnabled() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.world == null || mc.currentScreen == null || mc.player == null) {
            return false;
        }
        return enabled;
    }

    @Override
    public void doRender(MatrixStack ms, CustomRenderTypeBuffer buffer, float pt) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }


    }


    private void renderMultiblock(MatrixStack ms, CustomRenderTypeBuffer buffer, float pt, @NotNull BlockPos offset, @Nullable BlockPos hovering) {
        ClientWorld world = Minecraft.getInstance().world;
        int renderTicks = AnimationTickHelper.getTicks();
        float renderTimes = AnimationTickHelper.getRenderTime();

        for (Map.Entry<Vector3i, IMultiblockPart> entry : multiblock.getParts().entrySet()) {
            Vector3i relative = entry.getKey();
            List<BlockState> sampleBlocks = entry.getValue().getSampleBlocks();
            int i = (renderTicks) / 20 % sampleBlocks.size();

            BlockState block = sampleBlocks.get(i);
            BlockPos offPos = PositionUtils.applyModifies(relative, offset, rotation, isSymmetrical);


            BlockState current = world.getBlockState(offPos);
            ms.push();
            ms.translate(offPos.getX(), offPos.getY(), offPos.getZ());
            if (!entry.getValue().test(current)) {
                IModelData modelData = ModelDataManager.getModelData(world, offPos);

                //0xF000F0就是天空亮度15方块亮度15

                if (current.getBlock() != Blocks.AIR) {
                    //稍微放大一丁点，防止贴图错误
                    ms.scale(1.002f, 1.002f, 1.002f);
                    ms.translate(-0.001f, -0.001f, -0.001f);
                    //如果不对，显示红色边框，潜行的时候透视
                    IVertexBuilder builder = Minecraft.getInstance().player.isSneaking() ? buffer.getBuffer(ProjectionRenderType.LINE_NO_DEPTH) : buffer.getBuffer(RenderType.LINES);
                    WorldRenderer.drawBoundingBox(ms, builder,
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
                if (offPos.equals(hovering)) {
                    alpha = 0.6F + (float) (Math.sin(renderTimes * 0.2F) + 1F) * 0.15F;
                }
                //渲染投影
                RenderSystem.blendColor(1, 1, 1, alpha);
                RenderUtils.renderBlock(block, buffer, ms, 0xF000F0, modelData);
                buffer.finish(ProjectionRenderType.TRANSPARENT_BLOCK);
                RenderSystem.blendColor(1, 1, 1, 1);
            }
            ms.pop();
        }
    }

    @Override
    public void update() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.world == null) {
            enabled = false;
            return;
        }
        if (mc.player == null) {
            enabled = false;
            return;
        }

        //TODO: 物品当然要改成蓝图啦x
        ItemStack heldItem = mc.player.getHeldItem(Hand.MAIN_HAND);
        if (heldItem.getItem() == Items.PAPER) {
            enabled = true;
        } else {
            enabled = false;
        }

    }
}
