package com.nmmoc7.polymercore.client.utils.schematic;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nmmoc7.polymercore.client.renderer.CustomRenderTypeBuffer;
import com.nmmoc7.polymercore.client.renderer.CustomRenderTypes;
import com.nmmoc7.polymercore.client.utils.AnimationTickHelper;
import com.nmmoc7.polymercore.client.utils.GhostBlockUtils;
import com.nmmoc7.polymercore.client.utils.RenderUtils;
import com.nmmoc7.polymercore.client.utils.math.SchematicTransform;
import com.nmmoc7.polymercore.client.utils.multiblock.ISampleProvider;
import com.nmmoc7.polymercore.client.utils.multiblock.SchematicMultiblock;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraftforge.client.model.data.IModelData;

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
    private SchematicMultiblock schematicMultiblock;

    private int animatingTicks = 0;
    private int totalAnimatingTicks = 10;

    public void fadeOut(SchematicRenderer mainRenderer) {
        fadeOut(mainRenderer, 10);
    }

    public void fadeOut(SchematicRenderer mainRenderer, int animatingTicks) {
        if (mainRenderer == null || mainRenderer.getMultiblock() == null) {
            return;
        }
        this.originalTransform = mainRenderer.getTransform().copy().shrink(0.94f);
        this.targetTransform = originalTransform.copy().shrink(0.1f);
        this.schematicMultiblock = mainRenderer.getSchematicMultiblock();
        this.animatingTicks = animatingTicks;
        this.totalAnimatingTicks = animatingTicks;
    }

    public boolean isActivated() {
        return animatingTicks > 0 && schematicMultiblock != null;
    }

    public void clear() {
        this.schematicMultiblock = null;
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


        Map<Vector3i, ISampleProvider> parts = schematicMultiblock.getSamples(transform.isFlipped());

        Vector4f viewRelative = transformCamera(Minecraft.getInstance().gameRenderer.getMainCamera().getPosition(), transform);
        SortedMap<Double, Vector3i> map = sortByDistance(parts.keySet(), viewRelative);

        ms.pushPose();
        transform.applyEntirely(ms);
        for (Vector3i relativePos : map.values()) {
            ms.pushPose();

            ms.translate(transform.getFlip() * relativePos.getX(), relativePos.getY(), relativePos.getZ());
            transform.applyPartially(ms);

            //获取显示的样本
            BlockState block = pickupSampleBlock(renderTicks, parts.get(relativePos));
            IModelData modelData = findModelData(block, relativePos, schematicMultiblock.getOriginalMultiblock());
            //渲染投影
            RenderUtils.renderBlock(block, buffer, ms, 0xF000F0, false);

            ms.popPose();
        }
        buffer.finish();
        ms.popPose();

    }


}
