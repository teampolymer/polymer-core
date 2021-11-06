package com.nmmoc7.polymercore.client.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.client.renderer.CustomRenderTypeBuffer;
import com.nmmoc7.polymercore.client.renderer.IRenderer;
import com.nmmoc7.polymercore.client.utils.math.SchematicTransform;
import com.nmmoc7.polymercore.client.utils.schematic.SchematicRenderer;
import com.nmmoc7.polymercore.common.registry.KeysRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;

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

    //显示绑定的多方快结构
    private BlockPos offset;
    private Rotation rotation = Rotation.NONE;
    private boolean isSymmetrical;


    private static final SchematicRenderer renderer;
    private static SchematicTransform targetTransform;
    private static SchematicTransform originalTransform;
    private static int animatingTicks = 0;
    private static final int TOTAL_ANIMATION_TICK = 10;

    static {
        originalTransform = new SchematicTransform();
        renderer = new SchematicRenderer();
        renderer.setTransform(originalTransform);
    }


    @Override
    public boolean isEnabled() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.world == null || mc.player == null) {
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

        //动画效果
        if (animatingTicks > 0 && targetTransform != null) {
            float percent = ((TOTAL_ANIMATION_TICK - animatingTicks) + pt) / TOTAL_ANIMATION_TICK;
            renderer.getTransform().interpolateTo(percent, originalTransform, targetTransform);
        }


        renderer.render(ms, buffer, pt);

    }

    public void anchorIn(BlockPos pos) {
        if (pos == null) {
            isAnchored = false;
            return;
        }
        isAnchored = true;


        if (pos.equals(offset)) {
            isSymmetrical = !isSymmetrical;
        } else {
            offset = pos;
        }


        transformAnimated();

    }

    public void transformAnimated() {
        renderer.setAnchorPos(offset);
        renderer.setSymmetrical(isSymmetrical);
        renderer.setRotation(rotation);
        transformAnimated(SchematicTransform.create(offset, rotation, isSymmetrical));
    }

    public void transformAnimated(SchematicTransform transform) {
        originalTransform = renderer.getTransform().copy();
        targetTransform = transform;
        animatingTicks = TOTAL_ANIMATION_TICK;
        renderer.setAnimating(true);
    }


    public void onKeyInput(int key, boolean pressed) {
        if (!activating)
            return;
        if (!pressed)
            return;
        Minecraft mc = Minecraft.getInstance();

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
            if (currentMultiblock == null) {
                currentMultiblock = PolymerCoreApi.getInstance().getMultiblockManager()
                    .getDefinedMultiblock(new ResourceLocation(PolymerCoreApi.MOD_ID, "test_machine"))
                    .orElse(null);
                renderer.setMultiblock(currentMultiblock);
            }
            anchorIn(blockRayTraceResult.getPos());
        }
    }


    public boolean onMouseScrolled(double delta) {
        if (!activating)
            return false;
        if (KeysRegistry.TOOL_CTRL_KEY.isKeyDown()) {
            int dir = MathHelper.clamp((int) delta, -1, 1);
            int index = ((dir + rotation.ordinal()) % 4 + 4) % 4;
            rotation = Rotation.values()[index];
            transformAnimated();
            return true;
        }
        return false;
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

        if (animatingTicks > 0) {
            if (animatingTicks == 1) {
                renderer.setAnimating(false);
                originalTransform = targetTransform.copy();
                renderer.setTransform(targetTransform);
            }
            animatingTicks--;
        }


    }

}
