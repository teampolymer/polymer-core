package com.nmmoc7.polymercore.client.utils.schematic;

import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.util.PositionUtils;
import com.nmmoc7.polymercore.client.renderer.CustomRenderTypeBuffer;
import com.nmmoc7.polymercore.client.renderer.SchematicRenderTypes;
import com.nmmoc7.polymercore.client.utils.RenderUtils;
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
import net.minecraftforge.client.model.data.IModelData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 已经固定在某一个坐标的多方快投影
 */
public class AnchoredSchematicRenderer extends SchematicRenderer {

    private Map<BlockPos, IMultiblockPart> cachedRenderingParts = new HashMap<>();
    private AxisAlignedBB totalAABB;

    private IDefinedMultiblock multiblock;
    private Rotation rotation = Rotation.NONE;
    private boolean isSymmetrical = false;

    public void setAnchorPos(BlockPos anchorPos) {
        this.anchorPos = anchorPos;
    }

    private BlockPos anchorPos;

    private Map<BlockPos, IMultiblockPart> getRenderingParts() {
        if (cachedRenderingParts.isEmpty()) {
            for (Map.Entry<Vector3i, IMultiblockPart> entry : multiblock.getParts().entrySet()) {

                Vector3i relativePos = entry.getKey();
                BlockPos offPos = PositionUtils.applyModifies(relativePos, anchorPos, rotation, isSymmetrical);
                cachedRenderingParts.put(offPos, entry.getValue());

            }
        }

        return cachedRenderingParts;
    }


    @Override
    public void setMultiblock(IDefinedMultiblock multiblock) {
        this.multiblock = multiblock;
    }

    @Override
    public void render(MatrixStack ms, CustomRenderTypeBuffer buffer, float pt) {
        ClientWorld world = Minecraft.getInstance().world;
        int renderTicks = AnimationTickHolder.getTicks();
        float renderTimes = AnimationTickHolder.getRenderTime();

        BlockPos hovering = null;

        //获取玩家正在看向的方块
        if (Minecraft.getInstance().objectMouseOver instanceof BlockRayTraceResult) {
            BlockRayTraceResult rayTraceResult = (BlockRayTraceResult) Minecraft.getInstance().objectMouseOver;
            if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
                hovering = rayTraceResult.getPos().offset(rayTraceResult.getFace());
            }
        }

        for (Map.Entry<BlockPos, IMultiblockPart> entry : getRenderingParts().entrySet()) {
            BlockPos offPos = entry.getKey();
            List<BlockState> sampleBlocks = entry.getValue().getSampleBlocks();
            int i = (renderTicks) / 20 % sampleBlocks.size();
            BlockState block = sampleBlocks.get(i);
            BlockState current = world.getBlockState(offPos);
            ms.push();
            ms.translate(offPos.getX(), offPos.getY(), offPos.getZ());
            if (!entry.getValue().test(current)) {
                IModelData modelData = ModelDataManager.getModelData(world, offPos);

                //稍微缩小一点点
                ms.scale(0.94f, 0.94f, 0.94f);
                ms.translate(0.03f, 0.03f, 0.03f);

                if (current.getBlock() != Blocks.AIR) {
                    float alpha = 0.8F;
                    //渲染错误的方块
                    RenderSystem.blendColor(1, 1, 1, alpha);
                    RenderUtils.renderBlock(Blocks.RED_CONCRETE.getDefaultState(), buffer, ms, 0xF000F0, modelData);
                    buffer.finish(SchematicRenderTypes.TRANSPARENT_BLOCK);
                    RenderSystem.blendColor(1, 1, 1, 1);

                } else {
                    //默认透明度0.3
                    float alpha = 0.3F;
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
            }
            ms.pop();

        }
    }
}
