package com.nmmoc7.polymercore.client.utils.schematic;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.util.PositionUtils;
import com.nmmoc7.polymercore.client.renderer.CustomRenderTypeBuffer;
import com.nmmoc7.polymercore.client.renderer.SchematicRenderTypes;
import com.nmmoc7.polymercore.client.utils.AnimationTickHelper;
import com.nmmoc7.polymercore.client.utils.InputUtils;
import com.nmmoc7.polymercore.client.utils.RenderUtils;
import com.nmmoc7.polymercore.client.utils.SchematicRenderUtils;
import com.nmmoc7.polymercore.client.utils.math.SchematicTransform;
import it.unimi.dsi.fastutil.doubles.Double2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.doubles.DoubleComparators;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.*;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import static com.nmmoc7.polymercore.client.utils.SchematicRenderUtils.*;

/**
 * 已经固定在某一个坐标的多方快投影
 */
public class SchematicFadeOutRenderer {

    public SchematicFadeOutRenderer() {
        transform = new SchematicTransform();
    }


    private final SchematicTransform transform;
    private SchematicTransform originalTransform;
    private SchematicTransform targetTransform;

    //显示绑定的多方快结构
    private IDefinedMultiblock multiblock;

    private int animatingTicks = 0;
    private int totalAnimatingTicks = 10;

    public void fadeOut(SchematicRenderer mainRenderer) {
        fadeOut(mainRenderer, 10);
    }

    public void fadeOut(SchematicRenderer mainRenderer, int animatingTicks) {
        this.originalTransform = mainRenderer.getTransform().copy().shrink(0.94f);
        this.targetTransform = originalTransform.copy().shrink(0.1f);
        this.multiblock = mainRenderer.getMultiblock();
        this.animatingTicks = animatingTicks;
        this.totalAnimatingTicks = animatingTicks;
    }

    public boolean isActivated() {
        return animatingTicks > 0 && multiblock != null;
    }

    public void clear() {
        this.multiblock = null;
        this.originalTransform = null;
        this.targetTransform = null;
    }


    public void tick() {
        if (animatingTicks > 0) {
            if (animatingTicks == 1) {
                clear();
            }
            animatingTicks--;
        }
    }

    public void render(MatrixStack ms, CustomRenderTypeBuffer buffer, float pt) {
        if (!isActivated()) {
            return;
        }
        int renderTicks = AnimationTickHelper.getTicks();
        //动画效果
        float percent = ((totalAnimatingTicks - animatingTicks) + pt) / totalAnimatingTicks;
        transform.interpolateTo(percent, originalTransform, targetTransform);


        Map<Vector3i, IMultiblockPart> parts = multiblock.getParts();

        Vector4f viewRelative = transformCamera(Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView(), transform);
        SortedMap<Double, Vector3i> map = sortByDistance(parts, viewRelative);

        ms.push();
        transform.applyEntirely(ms);
        for (Vector3i relativePos : map.values()) {
            ms.push();

            ms.translate(transform.getFlip() * relativePos.getX(), relativePos.getY(), relativePos.getZ());
            transform.applyPartially(ms);

            //获取显示的样本
            BlockState block = pickupSampleBlock(renderTicks, parts.get(relativePos));
            IModelData modelData = findModelData(block, relativePos, multiblock);
            //渲染投影
            RenderUtils.renderBlock(block, buffer, ms, 0xF000F0, modelData, SchematicRenderTypes.TRANSPARENT_BLOCK);

            ms.pop();
        }
        buffer.finish();
        ms.pop();

    }


}
