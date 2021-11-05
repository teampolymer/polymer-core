package com.nmmoc7.polymercore.client.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.client.renderer.CustomRenderTypeBuffer;
import com.nmmoc7.polymercore.client.renderer.IRenderer;
import com.nmmoc7.polymercore.client.utils.schematic.AnchoredSchematicRenderer;
import com.nmmoc7.polymercore.client.utils.schematic.SchematicRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.glfw.GLFW;

/**
 * 渲染多方快结构蓝图投影
 */
public class MultiblockSchematicHandler implements IRenderer {
    public static final MultiblockSchematicHandler INSTANCE = new MultiblockSchematicHandler();


    private boolean activating = false;


    //多方快
    private static IDefinedMultiblock currentMultiblock;
    //固定
    private static boolean isAnchored = false;

    private static AnchoredSchematicRenderer staticRender = new AnchoredSchematicRenderer();


    @Override
    public boolean isEnabled() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.world == null || mc.currentScreen == null || mc.player == null) {
            return false;
        }
        //手持蓝图或者已经固定一个坐标就显示
        return activating || isAnchored;
    }

    @Override
    public void doRender(MatrixStack ms, CustomRenderTypeBuffer buffer, float pt) {
        if (currentMultiblock == null) {
            return;
        }
        staticRender.render(ms, buffer, pt);

    }

    public void anchorIn(BlockPos pos) {
        if (pos == null) {
            isAnchored = false;
            return;
        }
        isAnchored = true;

        staticRender.setAnchorPos(pos);
    }


    public void onKeyInput(int key, boolean pressed) {
        if (!activating)
            return;
        if (!pressed)
            return;
        Minecraft mc = Minecraft.getInstance();

        return;
    }

    public void onMouseInput(int button, boolean pressed) {
        if (!activating)
            return;
        if (!pressed || button != 1)
            return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player.isSneaking())
            return;
        ItemStack heldItem = mc.player.getHeldItem(Hand.MAIN_HAND);
        if (heldItem.getItem() != Items.PAPER) {
            return;
        }
        if (mc.objectMouseOver instanceof BlockRayTraceResult) {
            BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) mc.objectMouseOver;
//            BlockState clickedBlock = mc.world.getBlockState(blockRayTraceResult.getPos());
            currentMultiblock = PolymerCoreApi.getInstance().getMultiblockManager()
                .getDefinedMultiblock(new ResourceLocation(PolymerCoreApi.MOD_ID, "test_machine"))
                .orElse(null);
            staticRender.setMultiblock(currentMultiblock);
            anchorIn(blockRayTraceResult.getPos());
            return;
        }
        return;
    }


    @Override
    public void update() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.world == null) {
            activating = false;
            return;
        }
        if (mc.player == null) {
            activating = false;
            return;
        }

        //TODO: 物品当然要改成蓝图啦x
        ItemStack heldItem = mc.player.getHeldItem(Hand.MAIN_HAND);
        if (heldItem.getItem() == Items.PAPER) {
            activating = true;
        } else {
            activating = false;
        }

    }

}
