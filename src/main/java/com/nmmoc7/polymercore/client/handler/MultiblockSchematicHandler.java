package com.nmmoc7.polymercore.client.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nmmoc7.polymercore.api.capability.IMultiblockLocateHandler;
import com.nmmoc7.polymercore.api.capability.IMultiblockSupplier;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.util.MultiblockUtils;
import com.nmmoc7.polymercore.client.renderer.CustomRenderTypeBuffer;
import com.nmmoc7.polymercore.client.renderer.IRenderer;
import com.nmmoc7.polymercore.client.utils.math.SchematicTransform;
import com.nmmoc7.polymercore.client.utils.schematic.SchematicRenderer;
import com.nmmoc7.polymercore.common.capability.blueprint.CapabilityMultiblock;
import com.nmmoc7.polymercore.common.registry.KeysRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Optional;

/**
 * 渲染多方快结构蓝图投影
 */
public class MultiblockSchematicHandler implements IRenderer {
    public static final MultiblockSchematicHandler INSTANCE = new MultiblockSchematicHandler();

    private MultiblockSchematicHandler() {
        renderer = new SchematicRenderer();
    }

    private boolean enabled = false;


    //多方快
    private IDefinedMultiblock currentMultiblock;

    IMultiblockLocateHandler locateHandler = null;

    //动画相关
    private final int TOTAL_ANIMATION_TICK = 10;
    private final SchematicRenderer renderer;
    private SchematicTransform targetTransform;
    private SchematicTransform originalTransform;
    private int animatingTicks = 0;

    //其他
    private BlockPos lastTrackPos = BlockPos.ZERO;


    @Override
    public boolean isEnabled() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.world == null || mc.player == null) {
            return false;
        }
        //手持蓝图或者已经固定一个坐标就显示
        return enabled && currentMultiblock != null;
    }

    @Override
    public void doRender(MatrixStack ms, CustomRenderTypeBuffer buffer, float pt) {
        //动画效果
        if (animatingTicks > 0 && targetTransform != null) {
            float percent = ((TOTAL_ANIMATION_TICK - animatingTicks) + pt) / TOTAL_ANIMATION_TICK;
            renderer.getTransform().interpolateTo(percent, originalTransform, targetTransform);
        }


        renderer.render(ms, buffer, pt);

    }

    public void anchorIn(BlockPos pos) {
        if (locateHandler == null) {
            return;
        }
        if (pos == null) {
            locateHandler.setOffset(BlockPos.ZERO);
            locateHandler.setAnchored(false);
            return;
        }
        locateHandler.setOffset(pos);
        locateHandler.setAnchored(true);

        transformAnimated();

    }

    public void reset(boolean full) {
        renderer.setAnimating(false);
        originalTransform = new SchematicTransform();
        renderer.setTransform(originalTransform);
        targetTransform = null;
        if (full) {
            renderer.setMultiblock(currentMultiblock);
        }
    }


    public void rotate(int dir) {
        if (locateHandler == null) {
            return;
        }
        int current = locateHandler.getRotation().ordinal();
        int index = ((dir + current) % 4 + 4) % 4;
        Rotation rotation = Rotation.values()[index];
        locateHandler.setRotation(rotation);

        transformAnimated();
    }

    public boolean activating() {
        return currentMultiblock != null && locateHandler != null;
    }

    public void transformAnimated() {
        if (locateHandler == null) {
            return;
        }
        if (locateHandler.isAnchored()) {
            renderer.setOffset(locateHandler.getOffset());
            renderer.setSymmetrical(locateHandler.isFlipped());
            renderer.setRotation(locateHandler.getRotation());
            transformAnimated(SchematicTransform.create(locateHandler.getOffset(), locateHandler.getRotation(), locateHandler.isFlipped()));
        } else {
            renderer.setOffset(lastTrackPos);
            renderer.setSymmetrical(locateHandler.isFlipped());
            renderer.setRotation(locateHandler.getRotation());
            transformAnimated(SchematicTransform.create(lastTrackPos, locateHandler.getRotation(), locateHandler.isFlipped()));
        }
    }

    public void transformAnimated(SchematicTransform transform) {
        originalTransform = renderer.getTransform().copy();
        targetTransform = transform;
        animatingTicks = TOTAL_ANIMATION_TICK;
        renderer.setAnimating(true);
    }


    public void onKeyInput(int key, boolean pressed) {
        if (!activating())
            return;
        if (!pressed)
            return;
        Minecraft mc = Minecraft.getInstance();

    }

    public void onMouseInput(int button, boolean pressed) {
        if (!activating())
            return;
        if (!pressed || button != 1)
            return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player.isSneaking())
            return;

        if (KeysRegistry.TOOL_CTRL_KEY.isKeyDown()) {
            locateHandler.flip();
        } else {
            BlockPos tracePos = findTracePos();
            anchorIn(tracePos);
        }
    }


    public BlockPos findTracePos() {
        if (!activating()) {
            return null;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.objectMouseOver instanceof BlockRayTraceResult) {
            BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) mc.objectMouseOver;
            RayTraceResult.Type type = blockRayTraceResult.getType();
            if (type == RayTraceResult.Type.BLOCK) {
                BlockPos pos = blockRayTraceResult.getPos();
                Direction face = blockRayTraceResult.getFace();

                //处理机械
                return MultiblockUtils.findMostSuitablePosition(currentMultiblock, pos, face);

            }

        }
        return null;
    }


    public boolean onMouseScrolled(double delta) {
        if (!activating())
            return false;
        if (KeysRegistry.TOOL_CTRL_KEY.isKeyDown()) {
            int dir = MathHelper.clamp((int) delta, -1, 1);
            rotate(dir);
            return true;
        }
        return false;
    }

    private int elapsedTicks = 0;

    @Override
    public void update() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.world == null) {
            enabled = false;
            return;
        }
        if (mc.player == null) {
            enabled = false;
            return;
        }

        enabled = true;


        if (animatingTicks > 0) {
            if (animatingTicks == 1) {
                renderer.setAnimating(false);
                originalTransform = targetTransform.copy();
                renderer.setTransform(targetTransform);
            }
            animatingTicks--;
        }


        Optional<IMultiblockLocateHandler> locateHandler = findLocateHandler();
        if (locateHandler.isPresent()) {
            if (locateHandler.get() != this.locateHandler) {
                this.locateHandler = locateHandler.get();
                reset(false);
            }
        }

        if (elapsedTicks % 5 == 0) {
            findBlueprints();
        }
        elapsedTicks++;
        elapsedTicks %= 1000000;


        if (activating() && locateHandler.isPresent()) {
            BlockPos tracePos = findTracePos();
            if (tracePos != null && !tracePos.equals(lastTrackPos)) {
                lastTrackPos = tracePos;
                transformAnimated();
            }
        }


    }

    public Optional<IMultiblockLocateHandler> findLocateHandler() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return Optional.empty();
        }
        ItemStack heldItem = mc.player.getHeldItem(Hand.MAIN_HAND);
        LazyOptional<IMultiblockLocateHandler> capability = heldItem.getCapability(CapabilityMultiblock.MULTIBLOCK_LOCATE_HANDLER);
        if (!capability.isPresent()) {
            ItemStack heldOffhand = mc.player.getHeldItem(Hand.OFF_HAND);
            capability = heldOffhand.getCapability(CapabilityMultiblock.MULTIBLOCK_LOCATE_HANDLER);
        }
        return capability.resolve();


    }

    public void findBlueprints() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            this.currentMultiblock = null;
            return;
        }
        //左右手持
        ItemStack heldItem = mc.player.getHeldItem(Hand.MAIN_HAND);
        LazyOptional<IMultiblockSupplier> multiblockSupplier = heldItem.getCapability(CapabilityMultiblock.MULTIBLOCK_SUPPLIER);
        if (!multiblockSupplier.isPresent()) {
            ItemStack heldOffhand = mc.player.getHeldItem(Hand.OFF_HAND);
            multiblockSupplier = heldOffhand.getCapability(CapabilityMultiblock.MULTIBLOCK_SUPPLIER);
        }

        if (multiblockSupplier.isPresent()) {
            IDefinedMultiblock multiblock = multiblockSupplier.resolve().get().getMultiblock();
            if (multiblock != this.currentMultiblock) {
                this.currentMultiblock = multiblock;
                reset(true);
            }
            return;
        }

        PlayerInventory inv = mc.player.inventory;
        IDefinedMultiblock firstResult = null;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = inv.getStackInSlot(i);
            multiblockSupplier = stack.getCapability(CapabilityMultiblock.MULTIBLOCK_SUPPLIER);

            if (multiblockSupplier.isPresent()) {
                IDefinedMultiblock multiblock = multiblockSupplier.resolve().get().getMultiblock();
                //当前多方快不存在，直接赋值结束
                if (this.currentMultiblock == null) {
                    this.currentMultiblock = multiblock;
                    reset(true);
                    return;
                }

                if (firstResult == null) {
                    firstResult = multiblock;
                }
                //当前多方快存在，则判断物品栏里面是否存在正在投影的多方快
                if (multiblock == this.currentMultiblock) {
                    return;
                }
            }
        }

        if (firstResult != null) {
            this.currentMultiblock = firstResult;
            reset(true);
        }


    }

}
