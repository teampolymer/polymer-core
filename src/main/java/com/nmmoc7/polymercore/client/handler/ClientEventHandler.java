package com.nmmoc7.polymercore.client.handler;

import com.nmmoc7.polymercore.PolymerCore;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = PolymerCore.MOD_ID)
@OnlyIn(Dist.CLIENT)
public class ClientEventHandler {
    public static int elapsedTicks = 0;

    @SubscribeEvent
    public static void tickEnd(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.type != TickEvent.Type.CLIENT || event.side != LogicalSide.CLIENT) {
            return;
        }

        elapsedTicks++;

    }
}
