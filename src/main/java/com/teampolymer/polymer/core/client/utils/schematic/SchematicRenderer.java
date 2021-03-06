package com.teampolymer.polymer.core.client.utils.schematic;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.teampolymer.polymer.core.api.multiblock.IArchetypeMultiblock;
import com.teampolymer.polymer.core.api.multiblock.MultiblockDirection;
import com.teampolymer.polymer.core.api.multiblock.part.IMultiblockPart;
import com.teampolymer.polymer.core.api.multiblock.part.IPartChoice;
import com.teampolymer.polymer.core.api.multiblock.part.IPartLimitConfig;
import com.teampolymer.polymer.core.api.util.PositionUtils;
import com.teampolymer.polymer.core.client.renderer.CustomRenderTypeBuffer;
import com.teampolymer.polymer.core.client.renderer.CustomRenderTypes;
import com.teampolymer.polymer.core.client.utils.AnimationTickHelper;
import com.teampolymer.polymer.core.client.utils.GhostBlockUtils;
import com.teampolymer.polymer.core.client.utils.InputUtils;
import com.teampolymer.polymer.core.client.utils.RenderUtils;
import com.teampolymer.polymer.core.client.utils.math.SchematicTransform;
import com.teampolymer.polymer.core.client.utils.multiblock.ISampleProvider;
import com.teampolymer.polymer.core.client.utils.multiblock.SchematicMultiblock;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMaps;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import static com.teampolymer.polymer.core.client.utils.SchematicRenderUtils.*;

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
    private IArchetypeMultiblock multiblock;
    private Rotation rotation;
    private boolean isSymmetrical;

    private SchematicMultiblock schematicMultiblock;

    public SchematicMultiblock getSchematicMultiblock() {
        if (schematicMultiblock == null) {
            schematicMultiblock = new SchematicMultiblock(multiblock);
        }
        return schematicMultiblock;
    }


    public void setMultiblock(IArchetypeMultiblock multiblock) {
        this.schematicMultiblock = null;
        this.multiblock = multiblock;
    }

    public IArchetypeMultiblock getMultiblock() {
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
                notEmptyAreas.put(entry.getKey(), current.isAir());
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
                RenderUtils.renderBlock(block, buffer, ms, 0xF000F0, GhostBlockUtils.GhostRenderType.STATIC);
                ms.popPose();
            }
        } else {
            BlockPos hovering = InputUtils.getHoveringPos();
            transform.applyEntirely(ms);
            //检查错误的方块并渲染
            for (Vector3i relativePos : map.values()) {
                BlockPos offPos = PositionUtils.applyModifies(relativePos, offset, rotation, isSymmetrical);
                ISampleProvider sample = samples.get(relativePos);
                BlockState block = pickupSampleBlock(renderTicks, sample);

                ms.pushPose();

                //渲染投影
                if (notEmptyAreas.containsKey(relativePos)) {

                    ms.translate(transform.getFlip() * relativePos.getX(), relativePos.getY(), relativePos.getZ());
                    transform.applyPartially(ms);
                    if (!notEmptyAreas.getBoolean(relativePos)) {
                        //稍微缩小一点点
                        ms.translate(0.15f, 0.15f, 0.15f);
                        ms.scale(0.7f, 0.7f, 0.7f);

                        RenderUtils.renderBlock(block, buffer, ms, 0xF000F0, GhostBlockUtils.GhostRenderType.ERROR);

                    } else {
                        //稍微缩小一点点
                        ms.translate(0.03f, 0.03f, 0.03f);
                        ms.scale(0.94f, 0.94f, 0.94f);


                        RenderUtils.renderBlock(block, buffer, ms, 0xF000F0,
                            offPos.equals(hovering) ? GhostBlockUtils.GhostRenderType.ANIMATED : GhostBlockUtils.GhostRenderType.STATIC);

                    }
                }
                ms.popPose();

            }

//            buffer.finish();
        }

        ms.popPose();

    }
}
