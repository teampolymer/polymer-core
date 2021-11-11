package com.nmmoc7.polymercore.client.utils.schematic;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
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
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraftforge.client.model.data.IModelData;

import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;

import static com.nmmoc7.polymercore.client.utils.SchematicRenderUtils.*;

/**
 * 已经固定在某一个坐标的多方快投影
 */
public class SchematicRenderer {

    private AxisAlignedBB totalAABB;

    public SchematicRenderer() {
        reset(true);
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
    }

    public IDefinedMultiblock getMultiblock() {
        return multiblock;
    }

    public void reset(boolean hard) {
        if (hard) {
            animating = false;
            rotation = Rotation.NONE;
            isSymmetrical = false;
            offset = null;
            transform = new SchematicTransform();
        } else {
            transform = SchematicTransform.create(offset, rotation, isSymmetrical);
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
        if (offset == null) {
            return;
        }
        ClientWorld world = Minecraft.getInstance().world;
        int renderTicks = AnimationTickHelper.getTicks();
        Map<Vector3i, IMultiblockUnit> parts = Collections.emptyMap();
        Vector3d view = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();

        //转换视角
        Vector4f viewRelative = transformCamera(view, transform);

        SortedMap<Double, Vector3i> map = sortByDistance(parts, viewRelative);


        ms.push();
        if (animating) {
            transform.applyEntirely(ms);
            for (Vector3i relativePos : map.values()) {
                ms.push();
                ms.translate(transform.getFlip() * relativePos.getX(), relativePos.getY(), relativePos.getZ());
                transform.applyPartially(ms);
                //稍微缩小一点点
                ms.translate(0.03f, 0.03f, 0.03f);
                ms.scale(0.94f, 0.94f, 0.94f);

                //获取显示的样本
                BlockState block = pickupSampleBlock(renderTicks, parts.get(relativePos));
                IModelData modelData = findModelData(block, relativePos, multiblock);
                //渲染投影
                RenderUtils.renderBlock(block, buffer, ms, 0xF000F0, modelData, SchematicRenderTypes.TRANSPARENT_BLOCK);

                ms.pop();
            }


        } else {
            BlockPos hovering = InputUtils.getHoveringPos();
            //检查错误的方块并渲染
            for (Vector3i relativePos : map.values()) {
                BlockPos offPos = PositionUtils.applyModifies(relativePos, offset, rotation, isSymmetrical);
                //当前检查的方块
                BlockState current = world.getBlockState(offPos);
                IMultiblockUnit part = parts.get(relativePos);
                BlockState block = pickupSampleBlock(renderTicks, part);

                IModelData modelData = findModelData(block, relativePos, multiblock);

                ms.push();

                //渲染投影
                if (!part.test(current))
                    if (current.getBlock() != Blocks.AIR) {
                        ms.translate(offPos.getX(), offPos.getY(), offPos.getZ());

                        transform.applyPartially(ms);
                        //渲染错误的方块
                        RenderUtils.renderCube(ms, buffer.getBuffer(SchematicRenderTypes.CUBE_NO_DEPTH),
                            0.15f, 0.15f, 0.15f,
                            0.85f, 0.85f, 0.85f,
                            0.9f, 0.3f, 0.25f, 0.4f);
                    } else {
                        transform.applyEntirely(ms);
                        ms.translate(transform.getFlip() * relativePos.getX(), relativePos.getY(), relativePos.getZ());
                        transform.applyPartially(ms);
                        //稍微缩小一点点
                        ms.translate(0.03f, 0.03f, 0.03f);
                        ms.scale(0.94f, 0.94f, 0.94f);

                        RenderType renderType;
                        if (offPos.equals(hovering))
                            renderType = SchematicRenderTypes.TRANSPARENT_BLOCK_DYNAMIC;
                        else
                            renderType = SchematicRenderTypes.TRANSPARENT_BLOCK;

                        RenderUtils.renderBlock(block, buffer, ms, 0xF000F0, modelData, renderType);

                    }

                ms.pop();

            }
            buffer.finish();
        }

        ms.pop();

    }
}
