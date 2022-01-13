package com.nmmoc7.polymercore.client.event;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.client.handler.MultiblockSchematicHandler;
import com.nmmoc7.polymercore.client.renderer.CustomRenderTypeBuffer;
import com.nmmoc7.polymercore.client.utils.AnimationTickHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = PolymerCoreApi.MOD_ID)
@OnlyIn(Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void tickEnd(TickEvent.ClientTickEvent event) {
        if (isGameNotReady())
            return;
        if (event.type != TickEvent.Type.CLIENT || event.side != LogicalSide.CLIENT) {
            return;
        }
        if (event.phase == TickEvent.Phase.START) {
            MultiblockSchematicHandler.INSTANCE.update();
            return;
        }

        AnimationTickHelper.tick();


    }

    public static boolean isGameNotReady() {
        return Minecraft.getInstance().level == null || Minecraft.getInstance().player == null;
    }

    @SubscribeEvent
    public static void onLoadWorld(WorldEvent.Load event) {
        IWorld world = event.getWorld();
        if (world.isClientSide() && world instanceof ClientWorld) {
            AnimationTickHelper.reset();
        }
    }

    @SubscribeEvent
    public static void onUnloadWorld(WorldEvent.Unload event) {
        if (event.getWorld().isClientSide()) {
            AnimationTickHelper.reset();
        }
    }


    @SubscribeEvent
    public static void onWorldRenderLast(RenderWorldLastEvent event) {
        if (isGameNotReady())
            return;
        MatrixStack ms = event.getMatrixStack();
        ms.pushPose();
        //坐标向玩家视角偏移
        Vector3d view = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        ms.translate(-view.x, -view.y, -view.z);

        CustomRenderTypeBuffer buffer = CustomRenderTypeBuffer.instance();
        float pt = AnimationTickHelper.getPartialTicks();
        tickRenders(ms, buffer, pt);
        buffer.finish();
        ms.popPose();

    }


    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (isGameNotReady())
            return;
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        float pt = event.getPartialTicks();
        MatrixStack ms = event.getMatrixStack();
        ms.pushPose();
        MultiblockSchematicHandler.INSTANCE.renderOverlay(ms, pt);
        ms.popPose();
    }

    private static void tickRenders(MatrixStack ms, CustomRenderTypeBuffer buffer, float pt) {
        MultiblockSchematicHandler.INSTANCE.renderTick(ms, buffer, pt);
    }
}

