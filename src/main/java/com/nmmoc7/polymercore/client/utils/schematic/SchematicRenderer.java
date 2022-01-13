package com.nmmoc7.polymercore.client.utils.schematic;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.MultiblockDirection;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.multiblock.part.IPartChoice;
import com.nmmoc7.polymercore.api.multiblock.part.IPartLimitConfig;
import com.nmmoc7.polymercore.api.util.PositionUtils;
import com.nmmoc7.polymercore.client.renderer.CustomRenderTypeBuffer;
import com.nmmoc7.polymercore.client.renderer.CustomRenderTypes;
import com.nmmoc7.polymercore.client.utils.AnimationTickHelper;
import com.nmmoc7.polymercore.client.utils.GhostBlockUtils;
import com.nmmoc7.polymercore.client.utils.InputUtils;
import com.nmmoc7.polymercore.client.utils.RenderUtils;
import com.nmmoc7.polymercore.client.utils.math.SchematicTransform;
import com.nmmoc7.polymercore.client.utils.multiblock.ISampleProvider;
import com.nmmoc7.polymercore.client.utils.multiblock.SchematicMultiblock;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMaps;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.IModelData;

import java.util.*;

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

    private SchematicMultiblock schematicMultiblock;

    public SchematicMultiblock getSchematicMultiblock() {
        if (schematicMultiblock == null) {
            schematicMultiblock = new SchematicMultiblock(multiblock);
        }
        return schematicMultiblock;
    }


    public void setMultiblock(IDefinedMultiblock multiblock) {
        this.schematicMultiblock = null;
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

    private final Object2BooleanMap<Vector3i> notEmptyAreas = Object2BooleanMaps.synchronize(new Object2BooleanOpenHashMap<>());

    public void tick(World world) {
        if (multiblock == null || offset == null || isAnimating()) {
            return;
        }
        notEmptyAreas.clear();
        Set<String> settledParts = new HashSet<>();
        Map<Vector3i, IMultiblockPart> parts = multiblock.getParts();
        for (Map.Entry<Vector3i, IMultiblockPart> entry : parts.entrySet()) {

            IMultiblockPart part = entry.getValue();
            Vector3i relativePos = entry.getKey();
            BlockPos offPos = PositionUtils.applyModifies(relativePos, offset, rotation, isSymmetrical);
            //当前检查的方块
            BlockState current = world.getBlockState(offPos);
            IPartChoice choice = part.pickupChoice(current, MultiblockDirection.get(rotation, isSymmetrical));
            //找不到choice了，表示当前位置不合法
            if (choice == null) {
                notEmptyAreas.put(entry.getKey(), current.getBlock() == Blocks.AIR);
            } else {
                //TODO: 已经放置的方块，检查限制
            }
        }
        for (IPartLimitConfig limitConfig : multiblock.getLimitConfigs()) {

        }
    }

    public void render(MatrixStack ms, CustomRenderTypeBuffer buffer, float pt) {
        if (offset == null) {
            return;
        }
        int renderTicks = AnimationTickHelper.getTicks();
        Map<Vector3i, ISampleProvider> samples = getSchematicMultiblock().getSamples(transform.isFlipped());
        Vector3d view = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        //转换视角
        Vector4f viewRelative = transformCamera(view, transform);

        SortedMap<Double, Vector3i> map = sortByDistance(samples.keySet(), viewRelative);


        ms.pushPose();
        if (animating) {
            transform.applyEntirely(ms);
            for (Vector3i relativePos : map.values()) {
                ms.pushPose();
                ms.translate(transform.getFlip() * relativePos.getX(), relativePos.getY(), relativePos.getZ());
                transform.applyPartially(ms);
                //稍微缩小一点点
                ms.translate(0.03f, 0.03f, 0.03f);
                ms.scale(0.94f, 0.94f, 0.94f);

                //获取显示的样本
                BlockState block = pickupSampleBlock(renderTicks, samples.get(relativePos));
                //渲染投影
                RenderUtils.renderBlock(block, buffer, ms, 0xF000F0, false);
                ms.popPose();
            }


        } else {
            BlockPos hovering = InputUtils.getHoveringPos();
            //检查错误的方块并渲染
            for (Vector3i relativePos : map.values()) {
                BlockPos offPos = PositionUtils.applyModifies(relativePos, offset, rotation, isSymmetrical);
                ISampleProvider sample = samples.get(relativePos);
                BlockState block = pickupSampleBlock(renderTicks, sample);

                ms.pushPose();

                //渲染投影
                if (notEmptyAreas.containsKey(relativePos))
                    if (!notEmptyAreas.getBoolean(relativePos)) {
                        ms.translate(offPos.getX(), offPos.getY(), offPos.getZ());

                        transform.applyPartially(ms);
                        //渲染错误的方块
                        RenderUtils.renderCube(ms, buffer.getBuffer(CustomRenderTypes.CUBE_NO_DEPTH),
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

                        RenderUtils.renderBlock(block, buffer, ms, 0xF000F0, offPos.equals(hovering));

                    }

                ms.popPose();

            }

            RenderSystem.disableDepthTest();
            RenderSystem.depthFunc(515);
            buffer.finish();
        }

        ms.popPose();

    }
}
