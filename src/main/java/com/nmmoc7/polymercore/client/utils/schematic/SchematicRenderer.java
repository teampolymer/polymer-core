package com.nmmoc7.polymercore.client.utils.schematic;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.util.PositionUtils;
import com.nmmoc7.polymercore.client.renderer.CustomRenderTypeBuffer;
import com.nmmoc7.polymercore.client.renderer.SchematicRenderTypes;
import com.nmmoc7.polymercore.client.utils.AnimationTickHelper;
import com.nmmoc7.polymercore.client.utils.InputUtils;
import com.nmmoc7.polymercore.client.utils.RenderUtils;
import com.nmmoc7.polymercore.client.utils.math.SchematicTransform;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;

import java.util.*;

/**
 * 已经固定在某一个坐标的多方快投影
 */
public class SchematicRenderer {

    private AxisAlignedBB totalAABB;

    public SchematicRenderer() {

        reset();
    }


    public void setAnchorPos(BlockPos anchorPos) {
        this.offset = anchorPos;
    }

    private BlockPos offset;
    private SchematicTransform transform;


    //是否在显示动画效果，如果是，不检查方块和处理
    private boolean animating = false;
    //显示绑定的多方快结构
    private IDefinedMultiblock multiblock;
    private Rotation rotation;
    private boolean isSymmetrical;


    public void setMultiblock(IDefinedMultiblock multiblock) {
        this.multiblock = multiblock;
        reset();
    }

    public void reset() {
        animating = false;
        rotation = Rotation.NONE;
        isSymmetrical = false;
        if (offset == null) {
            transform = new SchematicTransform();
        } else {
            transform = SchematicTransform.create(offset, Rotation.NONE, false);
        }
    }

    public BlockPos getOffset() {
        return offset;
    }

    public void setOffset(BlockPos offset) {
        this.offset = offset;
    }

    public SchematicTransform getTransform() {
        return transform;
    }

    public void setTransform(SchematicTransform transform) {
        this.transform = transform;
    }

    public boolean isAnimating() {
        return animating;
    }

    public void setAnimating(boolean animating) {
        this.animating = animating;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    public boolean isSymmetrical() {
        return isSymmetrical;
    }

    public void setSymmetrical(boolean symmetrical) {
        isSymmetrical = symmetrical;
    }

    public void render(MatrixStack ms, CustomRenderTypeBuffer buffer, float pt) {
        ClientWorld world = Minecraft.getInstance().world;
        int renderTicks = AnimationTickHelper.getTicks();
        float renderTimes = AnimationTickHelper.getRenderTime();

        ms.push();
        if (animating) {
            transform.apply(ms);
            //默认透明度0.3
            float alpha = 0.3F;
            RenderSystem.blendColor(1, 1, 1, alpha);
            for (Map.Entry<Vector3i, IMultiblockPart> entry : multiblock.getParts().entrySet()) {
                ms.push();
                Vector3i relativePos = entry.getKey();
                ms.translate(relativePos.getX(), relativePos.getY(), relativePos.getZ());

                //稍微缩小一点点
                ms.scale(0.94f, 0.94f, 0.94f);
                ms.translate(0.03f, 0.03f, 0.03f);
                //获取显示的样本
                List<BlockState> sampleBlocks = entry.getValue().getSampleBlocks();
                int i = (renderTicks) / 20 % sampleBlocks.size();
                BlockState block = sampleBlocks.get(i);

                //TODO: 这里需要调用VirtualWorld以进行复杂模型的处理
                IModelData modelData = EmptyModelData.INSTANCE;


                //渲染投影
                RenderUtils.renderBlock(block, buffer, ms, 0xF000F0, modelData);

                ms.pop();
            }

            buffer.finish(SchematicRenderTypes.TRANSPARENT_BLOCK);
            RenderSystem.blendColor(1, 1, 1, 1);

        } else {
            BlockPos hovering = InputUtils.getHoveringPos();

            float alpha = 0.6F;
            RenderSystem.blendColor(1, 1, 1, alpha);

            //检查错误的方块并渲染
            for (Map.Entry<Vector3i, IMultiblockPart> entry : multiblock.getParts().entrySet()) {
                Vector3i relativePos = entry.getKey();
                BlockPos offPos = PositionUtils.applyModifies(relativePos, offset, rotation, isSymmetrical);
                //当前检查的方块
                BlockState current = world.getBlockState(offPos);
                ms.push();
                ms.translate(offPos.getX(), offPos.getY(), offPos.getZ());
                if (!entry.getValue().test(current) && current.getBlock() != Blocks.AIR) {

                    //获取显示的样本
                    List<BlockState> sampleBlocks = entry.getValue().getSampleBlocks();
                    int i = (renderTicks) / 20 % sampleBlocks.size();
                    BlockState block = sampleBlocks.get(i);
                    //稍微缩小一点点
                    ms.scale(0.8f, 0.8f, 0.8f);
                    ms.translate(0.1f, 0.1f, 0.1f);
                    //渲染错误的方块
                    RenderUtils.renderBlock(Blocks.RED_CONCRETE.getDefaultState(), buffer, ms, 0xF000F0, EmptyModelData.INSTANCE);
//                    RenderUtils.renderBlock(block, buffer, ms, 0xF000F0, EmptyModelData.INSTANCE);
                }
                ms.pop();

            }
            buffer.finish(SchematicRenderTypes.TRANSPARENT_BLOCK);
            RenderSystem.blendColor(1, 1, 1, 1);


            //渲染其他方块
            transform.apply(ms);
            for (Map.Entry<Vector3i, IMultiblockPart> entry : multiblock.getParts().entrySet()) {
                Vector3i relativePos = entry.getKey();
                BlockPos offPos = PositionUtils.applyModifies(relativePos, offset, rotation, isSymmetrical);


                //当前检查的方块
                BlockState current = world.getBlockState(offPos);


                ms.push();
                ms.translate(relativePos.getX(), relativePos.getY(), relativePos.getZ());
                //稍微缩小一点点
                ms.scale(0.94f, 0.94f, 0.94f);
                ms.translate(0.03f, 0.03f, 0.03f);
                if (!entry.getValue().test(current) && current.getBlock() == Blocks.AIR) {
                    IModelData modelData = ModelDataManager.getModelData(world, offPos);

                    //获取显示的样本
                    List<BlockState> sampleBlocks = entry.getValue().getSampleBlocks();
                    int i = (renderTicks) / 20 % sampleBlocks.size();
                    BlockState block = sampleBlocks.get(i);
                    //默认透明度0.3
                    alpha = 0.3F;
                    //对当前选中的方块提高透明度
                    if (offPos.equals(hovering)) {
                        alpha = 0.6F + (float) (Math.sin(renderTimes * 0.2F) + 1F) * 0.15F;
                    }
                    //渲染投影
                    RenderSystem.blendColor(1, 1, 1, alpha);
                    RenderUtils.renderBlock(block, buffer, ms, 0xF000F0, modelData);
                    buffer.finish(SchematicRenderTypes.TRANSPARENT_BLOCK);
                    RenderSystem.blendColor(1, 1, 1, 1);

                }
                ms.pop();

            }
        }
        ms.pop();
    }
}
