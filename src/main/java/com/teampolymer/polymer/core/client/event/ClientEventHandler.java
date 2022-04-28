package com.teampolymer.polymer.core.client.event;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.teampolymer.polymer.core.api.PolymerCoreApi;
import com.teampolymer.polymer.core.client.handler.MultiblockSchematicHandler;
import com.teampolymer.polymer.core.client.renderer.CustomRenderTypeBuffer;
import com.teampolymer.polymer.core.client.renderer.CustomRenderTypes;
import com.teampolymer.polymer.core.client.shader.VanillaShaderManager;
import com.teampolymer.polymer.core.client.utils.AnimationTickHelper;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.profiler.IProfiler;
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
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.opengl.GL11;

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

            IProfiler profiler = Minecraft.getInstance().getProfiler();
            profiler.push("polymer-schematic-update");
            MultiblockSchematicHandler.INSTANCE.update();
            profiler.pop();
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


        IProfiler profiler = Minecraft.getInstance().getProfiler();
        profiler.push("polymer-render-world");
        profiler.push("shader-pre");
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

        VanillaShaderManager.forEachShader(it -> it.beforeRender(pt));

        profiler.popPush("tickRenders");
        RenderSystem.disableDepthTest();
        RenderSystem.depthFunc(GL11.GL_ALWAYS);
        MatrixStack ms = event.getMatrixStack();
        ms.pushPose();
        //坐标向玩家视角偏移
        Vector3d view = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        ms.translate(-view.x, -view.y, -view.z);

        CustomRenderTypeBuffer buffer = CustomRenderTypeBuffer.instance();
        tickRenders(ms, buffer, pt);
        buffer.finish();
        ms.popPose();

        profiler.popPush("shader-post");
        VanillaShaderManager.forEachShader(it -> it.postRender(pt));

        profiler.pop();
        profiler.pop();

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

        IProfiler profiler = Minecraft.getInstance().getProfiler();
        profiler.push("schematic-render");
        MultiblockSchematicHandler.INSTANCE.renderTick(ms, buffer, pt);
        profiler.pop();
    }
}

