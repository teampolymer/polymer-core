package com.nmmoc7.polymercore.common.handler;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PolymerCoreApi.MOD_ID)
public class PlayerHandler {
    @SubscribeEvent
    public static void onPlayerRightClick(PlayerInteractEvent.RightClickBlock e) {
        World world = e.getPlayer().level;
        if (world.isClientSide) {
            return;
        }
    }
}
