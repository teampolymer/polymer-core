package com.nmmoc7.polymercore.common.handler;

import com.nmmoc7.polymercore.PolymerCore;
import com.nmmoc7.polymercore.core.event.BlockStateChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PolymerCore.MOD_ID)
public class BlockStateChangeHandler {
    @SubscribeEvent
    public static void onBlockStateChange(BlockStateChangeEvent event) {
        System.out.println("Block State Change: " + event.getBlockStateIn() + "->" + event.getNewState());
    }
}
