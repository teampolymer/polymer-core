package com.nmmoc7.polymercore.client.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nmmoc7.polymercore.api.capability.IMultiblockLocateHandler;
import com.nmmoc7.polymercore.api.capability.IMultiblockSupplier;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.util.MultiblockUtils;
import com.nmmoc7.polymercore.client.gui.schematic.SchematicViewOverlay;
import com.nmmoc7.polymercore.client.renderer.CustomRenderTypeBuffer;
import com.nmmoc7.polymercore.client.renderer.IRenderer;
import com.nmmoc7.polymercore.client.utils.AnimationTickHelper;
import com.nmmoc7.polymercore.client.utils.math.SchematicTransform;
import com.nmmoc7.polymercore.client.utils.schematic.SchematicFadeOutRenderer;
import com.nmmoc7.polymercore.client.utils.schematic.SchematicRenderer;
import com.nmmoc7.polymercore.common.capability.blueprint.CapabilityMultiblockItem;
import com.nmmoc7.polymercore.common.network.ModNetworking;
import com.nmmoc7.polymercore.common.network.PacketLocateHandlerSync;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Optional;

/**
 * 渲染多方快结构蓝图投影
 */
public class MultiblockSchematicHandler implements IRenderer {
    public static final MultiblockSchematicHandler INSTANCE = new MultiblockSchematicHandler();

    private MultiblockSchematicHandler() {
        renderer = new SchematicRenderer();
        fadeOutRenderer = new SchematicFadeOutRenderer();
        viewOverlay = new SchematicViewOverlay(() -> {
            sync();
            lastTraceResult = null;
            transformAnimated();
        });
    }

    private boolean enabled = false;


    //多方快
    private IDefinedMultiblock currentMultiblock;

    private IMultiblockLocateHandler locateHandler = null;
    private int handleSlot = -1;

    //是否后台投影
    private boolean renderBackground = false;

    //动画相关
    private final int DEFAULT_TOTAL_ANIMATION_TICK = 10;
    private final SchematicRenderer renderer;
    private final SchematicFadeOutRenderer fadeOutRenderer;
    private SchematicTransform targetTransform;
    private SchematicTransform originalTransform;
    private int animatingTicks = 0;
    private int totalAnimatingTicks = DEFAULT_TOTAL_ANIMATION_TICK;

    //Overlay
    private final SchematicViewOverlay viewOverlay;

    //其他
    private BlockPos lastTrackPos = null;


    @Override
    public boolean isEnabled() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
            return false;
        }
        //手持蓝图或者已经固定一个坐标就显示
        return enabled && shouldRenderMain() || fadeOutRenderer.isActivated();
    }

    public boolean shouldRenderMain() {
        return currentMultiblock != null && (renderBackground || locateHandler != null);
    }

    @Override
    public void doRender(MatrixStack ms, CustomRenderTypeBuffer buffer, float pt) {
        //动画效果
        if (animatingTicks > 0 && targetTransform != null) {
            float percent = ((totalAnimatingTicks - animatingTicks) + pt) / totalAnimatingTicks;
            renderer.getTransform().interpolateTo(percent, originalTransform, targetTransform);
        }

        fadeOutRenderer.render(ms, buffer, pt);
        if (shouldRenderMain()) {
            //判断鼠标指向的方块，必须
            if (activating() && this.locateHandler != null && !this.locateHandler.isAnchored()) {
                BlockPos tracePos = findTracePos();
                if (tracePos != null && !tracePos.equals(lastTrackPos)) {
                    lastTrackPos = tracePos;
                    transformAnimated(6);
                }

                if (!viewOverlay.currentAction().shouldHideSchematic()) {
                    renderer.render(ms, buffer, pt);
                }
            } else {
                renderer.render(ms, buffer, pt);
            }

        }

    }


    public void renderOverlay(MatrixStack ms, float pt) {
        if (locateHandler != null) {
            viewOverlay.renderOverlay(ms, pt);
        }
    }

    public void fadeOutCurrent() {
        if (activating() && this.locateHandler != null && !this.locateHandler.isAnchored() && viewOverlay.currentAction().shouldHideSchematic()) {
            return;
        }
        fadeOutRenderer.fadeOut(renderer);
    }

    public void reset(boolean fullReset, boolean fade) {

        this.animatingTicks = 0;
        this.totalAnimatingTicks = DEFAULT_TOTAL_ANIMATION_TICK;
        lastTraceResult = null;
        lastTrackPos = findTracePos();

        boolean doFade = fade && currentMultiblock != null && locateHandler != null;

        if (!fullReset && !fade) {
            if (targetTransform != null) {
                originalTransform = targetTransform.copy();
                renderer.setTransform(targetTransform);
            } else {
                originalTransform = new SchematicTransform();
                renderer.setTransform(new SchematicTransform());
            }
            return;
        }

        //重置渲染
        renderer.setAnimating(false);
        if (fullReset) {
            renderBackground = false;
            renderer.setMultiblock(currentMultiblock);
            if (locateHandler == null) {
                renderer.reset(true);
                originalTransform = new SchematicTransform();
                renderer.setTransform(originalTransform);
                targetTransform = null;

            }
        }

        if (doFade || (fullReset && locateHandler != null)) {

            BlockPos offset = locateHandler.getOffset();
            renderer.setOffset(offset == null || !locateHandler.isAnchored() ? lastTrackPos : offset);
            renderer.setSymmetrical(locateHandler.isFlipped());
            renderer.setRotation(locateHandler.getRotation());
            renderer.reset(false);
            originalTransform = renderer.getTransform().copy();
        }

        if (doFade) {
            renderer.getTransform().shrink(0.1f);
            transformAnimated(10);
        }


    }

    public IDefinedMultiblock getCurrentMultiblock() {
        return currentMultiblock;
    }

    public void sync() {
        if (handleSlot >= -1 && locateHandler != null) {
            ModNetworking.INSTANCE.sendToServer(new PacketLocateHandlerSync(handleSlot, locateHandler));
        }
    }

    public boolean activating() {
        return currentMultiblock != null && locateHandler != null;
    }

    public void transformAnimated() {
        transformAnimated(DEFAULT_TOTAL_ANIMATION_TICK);
    }

    public void transformAnimated(int animatingTicks) {
        if (locateHandler == null) {
            return;
        }
        if (locateHandler.isAnchored()) {
            renderer.setOffset(locateHandler.getOffset());
            renderer.setSymmetrical(locateHandler.isFlipped());
            renderer.setRotation(locateHandler.getRotation());
            transformAnimated(
                SchematicTransform.create(locateHandler.getOffset(), locateHandler.getRotation(), locateHandler.isFlipped()),
                animatingTicks
            );
        } else {
            renderer.setOffset(lastTrackPos);
            renderer.setSymmetrical(locateHandler.isFlipped());
            renderer.setRotation(locateHandler.getRotation());
            if (lastTrackPos != null) {
                transformAnimated(
                    SchematicTransform.create(lastTrackPos, locateHandler.getRotation(), locateHandler.isFlipped()),
                    animatingTicks
                );
            }
        }
    }

    public void transformAnimated(SchematicTransform transform, int animatingTicks) {
        originalTransform = renderer.getTransform().copy();
        targetTransform = transform;
        this.animatingTicks = animatingTicks;
        this.totalAnimatingTicks = animatingTicks;
        renderer.setAnimating(true);
    }


    public void onKeyInput(int key, boolean pressed) {
        if (!activating())
            return;
        viewOverlay.handleKeyInput(locateHandler, key, pressed);

    }

    public void onMouseInput(int button, boolean pressed) {
        if (!activating())
            return;
        viewOverlay.handleMouseInput(locateHandler, button, pressed);
    }


    private BlockPos lastRaytracePos;
    private Direction lastFace;
    private BlockPos lastTraceResult;

    public BlockPos findTracePos() {
        if (!activating()) {
            return null;
        }
        Minecraft mc = Minecraft.getInstance();

        final int distance = 32;

        Vector3d start = mc.player.getEyePosition(AnimationTickHelper.getPartialTicks());
        Vector3d direction = mc.player.getViewVector(AnimationTickHelper.getPartialTicks());
        Vector3d end = start.add(direction.x * distance, direction.y * distance, direction.z * distance);
        BlockRayTraceResult blockRayTraceResult = mc.level.clip(
            new RayTraceContext(start, end,
                RayTraceContext.BlockMode.OUTLINE,
                RayTraceContext.FluidMode.NONE,
                mc.player)
        );

        RayTraceResult.Type type = blockRayTraceResult.getType();
        if (type == RayTraceResult.Type.BLOCK && !blockRayTraceResult.isInside()) {
            BlockPos pos = blockRayTraceResult.getBlockPos();
            Direction face = blockRayTraceResult.getDirection();

            if (!pos.equals(lastRaytracePos) || !face.equals(lastFace) || lastTraceResult == null) {
                lastRaytracePos = pos;
                lastFace = face;

                //处理机械
                lastTraceResult = MultiblockUtils.findMostSuitablePosition(
                    currentMultiblock,
                    pos,
                    face,
                    locateHandler.getRotation(),
                    locateHandler.isFlipped()
                );
            }


            return lastTraceResult;

        }


        return null;
    }


    public boolean onMouseScrolled(double delta) {
        if (!activating())
            return false;
        return viewOverlay.handleMouseScrolled(locateHandler, delta);
    }


    @Override
    public void update() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
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

        fadeOutRenderer.tick();
        renderer.tick(mc.level);
        viewOverlay.update(locateHandler);


        Tuple<Integer, Optional<IMultiblockLocateHandler>> locateHandlerWithSlot = findLocateHandler();
        Optional<IMultiblockLocateHandler> locateHandler = locateHandlerWithSlot.getB();
        if (locateHandler.isPresent()) {
            if (this.handleSlot != locateHandlerWithSlot.getA() || !locateHandler.get().equalsIgnoringSetting(this.locateHandler)) {
                //上一个没有手持控制器也没有后台渲染的蓝图
                boolean fade = this.locateHandler == null && !renderBackground;
                this.locateHandler = locateHandler.get();
                this.handleSlot = locateHandlerWithSlot.getA();
                reset(false, fade);
            }
        } else if (this.locateHandler != null) {
            this.renderBackground = this.locateHandler.isAnchored();
            if (!renderBackground) {
                fadeOutCurrent();
            }
            this.handleSlot = -2;
            this.locateHandler = null;

            reset(false, false);
        }

        findBlueprints();


    }

    public Tuple<Integer, Optional<IMultiblockLocateHandler>> findLocateHandler() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return new Tuple<>(-2, Optional.empty());
        }
        int slot = mc.player.inventory.selected;
        ItemStack heldItem = mc.player.getItemInHand(Hand.MAIN_HAND);
        LazyOptional<IMultiblockLocateHandler> capability = heldItem.getCapability(CapabilityMultiblockItem.MULTIBLOCK_LOCATE_HANDLER);
        if (!capability.isPresent()) {
            ItemStack heldOffhand = mc.player.getItemInHand(Hand.OFF_HAND);
            capability = heldOffhand.getCapability(CapabilityMultiblockItem.MULTIBLOCK_LOCATE_HANDLER);
            slot = -1;
        }
        return new Tuple<>(slot, capability.resolve());


    }

    public void findBlueprints() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            this.currentMultiblock = null;
            return;
        }
        //左右手持
        ItemStack heldItem = mc.player.getItemInHand(Hand.MAIN_HAND);
        LazyOptional<IMultiblockSupplier> multiblockSupplier = heldItem.getCapability(CapabilityMultiblockItem.MULTIBLOCK_SUPPLIER);
        if (!multiblockSupplier.isPresent()) {
            ItemStack heldOffhand = mc.player.getItemInHand(Hand.OFF_HAND);
            multiblockSupplier = heldOffhand.getCapability(CapabilityMultiblockItem.MULTIBLOCK_SUPPLIER);
        }

        if (multiblockSupplier.isPresent()) {
            IDefinedMultiblock multiblock = multiblockSupplier.resolve().get().getMultiblock();
            if (multiblock != this.currentMultiblock) {
                fadeOutCurrent();
                this.currentMultiblock = multiblock;
                reset(true, true);
            }
            return;
        }

        PlayerInventory inv = mc.player.inventory;
        IDefinedMultiblock firstResult = null;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = inv.getItem(i);
            multiblockSupplier = stack.getCapability(CapabilityMultiblockItem.MULTIBLOCK_SUPPLIER);

            if (multiblockSupplier.isPresent()) {
                IDefinedMultiblock multiblock = multiblockSupplier.resolve().get().getMultiblock();
                //当前多方快不存在，直接赋值结束
                if (this.currentMultiblock == null) {
                    fadeOutCurrent();
                    this.currentMultiblock = multiblock;
                    reset(true, true);
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
        if (this.currentMultiblock != firstResult) {
            fadeOutCurrent();
            this.currentMultiblock = firstResult;
            reset(true, firstResult != null);
        }

    }

}
