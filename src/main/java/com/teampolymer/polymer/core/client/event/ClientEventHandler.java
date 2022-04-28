package com.teampolymer.polymer.core.client.event;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.teampolymer.polymer.core.api.PolymerCoreApi;
import com.teampolymer.polymer.core.client.handler.MultiblockSchematicHandler;
import com.teampolymer.polymer.core.client.renderer.CustomRenderTypeBuffer;
import com.teampolymer.polymer.core.client.renderer.CustomRenderTypes;
import com.teampolymer.polymer.core.client.shader.VanillaShaderManager;
import com.teampolymer.polymer.core.client.utils.AnimationTickHelper;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
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

    private static int width = -1;
    private static int height = -1;

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

        Minecraft mc = Minecraft.getInstance();
        MainWindow mainWindow = mc.getWindow();
        int width = mainWindow.getWidth();
        int height = mainWindow.getHeight();
        if (ClientEventHandler.width != width || ClientEventHandler.height != height) {
            ClientEventHandler.width = width;
            ClientEventHandler.height = height;

            VanillaShaderManager.forEachShader(it -> it.resize(width, height));
        }

        float pt = AnimationTickHelper.getPartialTicks();

        VanillaShaderManager.forEachShader(it ->
            it.beforeRender(pt)
        );

        MatrixStack ms = event.getMatrixStack();
        ms.pushPose();
        //坐标向玩家视角偏移
        Vector3d view = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        ms.translate(-view.x, -view.y, -view.z);

        CustomRenderTypeBuffer buffer = CustomRenderTypeBuffer.instance();
        tickRenders(ms, buffer, pt);
        buffer.finish();
        buffer.finish(CustomRenderTypes.TRANSPARENT_BLOCK);
        buffer.finish(CustomRenderTypes.TRANSPARENT_BLOCK_DYNAMIC);
        ms.popPose();

        VanillaShaderManager.forEachShader(it ->
            it.render(pt)
        );

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

