package com.nmmoc7.polymercore.client.event;

import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.client.handler.MultiblockSchematicHandler;
import com.nmmoc7.polymercore.client.renderer.CustomRenderTypeBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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



    }

    public static boolean isGameNotReady() {
        return Minecraft.getInstance().world == null || Minecraft.getInstance().player == null;
    }


    @SubscribeEvent
    public static void onWorldRenderLast(RenderWorldLastEvent event) {
        if (isGameNotReady())
        return;
        MatrixStack ms = event.getMatrixStack();
        ms.push();
        //坐标向玩家视角偏移
        Vector3d view = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
        ms.translate(-view.x, -view.y, -view.z);

        CustomRenderTypeBuffer buffer = CustomRenderTypeBuffer.instance();
        float pt = AnimationTickHolder.getPartialTicks();
        tickRenders(ms, buffer, pt);

        buffer.finish();
        ms.pop();
    }

    private static void tickRenders(MatrixStack ms, CustomRenderTypeBuffer buffer, float pt) {
        MultiblockSchematicHandler.INSTANCE.renderTick(ms, buffer, pt);
    }
}

