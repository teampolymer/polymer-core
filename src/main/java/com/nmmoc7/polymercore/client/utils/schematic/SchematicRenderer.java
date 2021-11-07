package com.nmmoc7.polymercore.client.utils.schematic;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
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
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import org.lwjgl.opengl.GL11;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 已经固定在某一个坐标的多方快投影
 */
public class SchematicRenderer {

    private AxisAlignedBB totalAABB;

    public SchematicRenderer() {

        reset();
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
        Map<Vector3i, IMultiblockPart> parts = multiblock.getParts();
        Vector3d view = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();

        Stream<Tuple<Vector3i, BlockPos>> entries = parts
            .keySet()
            .stream()
            .map(it -> new Tuple<>(it, PositionUtils.applyModifies(it, offset, rotation, isSymmetrical)))
            .sorted(
                Collections.reverseOrder(
                    Comparator
                        .comparingDouble(it ->
                            view.squareDistanceTo(it.getB().getX(), it.getB().getY(), it.getB().getZ())
                        )
                )
            );

        if (transform.flip < 0) {
            GL11.glFrontFace(GL11.GL_CW);
        }

        ms.push();
        if (animating) {
            transform.apply(ms);
            //默认透明度0.3
            float alpha = 0.3F;
            RenderSystem.blendColor(1, 1, 1, alpha);
            entries.forEachOrdered(entry -> {
                ms.push();

                Vector3i relativePos = entry.getA();
                ms.translate(relativePos.getX(), relativePos.getY(), relativePos.getZ());

                //稍微缩小一点点
                ms.scale(0.94f, 0.94f, 0.94f);
                ms.translate(0.03f, 0.03f, 0.03f);
                //获取显示的样本
                List<BlockState> sampleBlocks = parts.get(relativePos).getSampleBlocks();
                int i = (renderTicks) / 20 % sampleBlocks.size();
                BlockState block = sampleBlocks.get(i);

                //TODO: 这里需要调用VirtualWorld以进行复杂模型的处理
                IModelData modelData = EmptyModelData.INSTANCE;


                //渲染投影
                RenderUtils.renderBlock(block, buffer, ms, 0xF000F0, modelData);

                buffer.finish(SchematicRenderTypes.TRANSPARENT_BLOCK);
                ms.pop();
            });
            RenderSystem.blendColor(1, 1, 1, 1);


        } else {
            BlockPos hovering = InputUtils.getHoveringPos();
            final Set<BlockPos> allWrong = new HashSet<>();
            //检查错误的方块并渲染
            entries.forEachOrdered(entry -> {
                Vector3i relativePos = entry.getA();
//                BlockPos offPos = PositionUtils.applyModifies(relativePos, offset, rotation, isSymmetrical);
                BlockPos offPos = entry.getB();
                //当前检查的方块
                BlockState current = world.getBlockState(offPos);

                IMultiblockPart part = parts.get(relativePos);
                //获取显示的样本
                List<BlockState> sampleBlocks = part.getSampleBlocks();
                int i = (renderTicks) / 20 % sampleBlocks.size();
                BlockState block = sampleBlocks.get(i);

                IModelData modelData = ModelDataManager.getModelData(world, offPos);

                ms.push();

                //渲染投影
                if (!part.test(current))
                    if (current.getBlock() != Blocks.AIR) {
                        allWrong.add(offPos);
                    } else {
                        transform.apply(ms);
                        ms.translate(relativePos.getX(), relativePos.getY(), relativePos.getZ());
                        //稍微缩小一点点
                        ms.scale(0.94f, 0.94f, 0.94f);
                        ms.translate(0.03f, 0.03f, 0.03f);

                        RenderUtils.renderBlock(block, buffer, ms, 0xF000F0, modelData);

                        //默认透明度0.3
                        float alpha = 0.3F;
                        //对当前选中的方块提高透明度
                        if (offPos.equals(hovering))
                            alpha = 0.6F + (float) (Math.sin(renderTimes * 0.2F) + 1F) * 0.15F;
                        RenderSystem.blendColor(1, 1, 1, alpha);
                        buffer.finish(SchematicRenderTypes.TRANSPARENT_BLOCK);
                        RenderSystem.blendColor(1, 1, 1, 1);

                    }

                ms.pop();

            });

            for (BlockPos offPos : allWrong) {
                ms.push();
                ms.translate(offPos.getX(), offPos.getY(), offPos.getZ());
                //渲染错误的方块
                RenderUtils.renderCube(ms, buffer.getBuffer(SchematicRenderTypes.CUBE_NO_DEPTH),
                    0.15f, 0.15f, 0.15f,
                    0.85f, 0.85f, 0.85f,
                    1.0f, 0.3f, 0.3f, 0.3f);
                ms.pop();
            }
            buffer.finish(SchematicRenderTypes.CUBE_NO_DEPTH);
        }

        ms.pop();

        GL11.glFrontFace(GL11.GL_CCW);
    }
}
