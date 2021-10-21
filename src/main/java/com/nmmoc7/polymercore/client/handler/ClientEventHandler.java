package com.nmmoc7.polymercore.client.handler;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.client.utils.AnimationTickHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
        if (!isGameActive())
            return;
        if (event.type != TickEvent.Type.CLIENT || event.side != LogicalSide.CLIENT) {
            return;
        }
        if (event.phase == TickEvent.Phase.START) {
            return;
        }

        AnimationTickHelper.tick();

    }

    public static boolean isGameActive() {
        return !(Minecraft.getInstance().world == null || Minecraft.getInstance().player == null);
    }

    @SubscribeEvent
    public static void onLoadWorld(WorldEvent.Load event) {
        IWorld world = event.getWorld();
        if (world.isRemote() && world instanceof ClientWorld) {
            AnimationTickHelper.reset();
        }
    }

    @SubscribeEvent
    public static void onUnloadWorld(WorldEvent.Unload event) {
        if (event.getWorld().isRemote()) {
            AnimationTickHelper.reset();
        }
    }
}

