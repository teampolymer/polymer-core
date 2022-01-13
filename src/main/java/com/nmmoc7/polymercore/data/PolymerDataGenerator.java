package com.nmmoc7.polymercore.data;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.data.loottable.PolymerLootModifierProvider;
import com.nmmoc7.polymercore.data.loottable.PolymerLootTableProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PolymerDataGenerator {
    @SubscribeEvent
    public static void gather(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        if (event.includeServer()) {
            gen.addProvider(new PolymerLootTableProvider(gen));
//            gen.addProvider(new PolymerLootModifierProvider(gen, PolymerCoreApi.MOD_ID));
        }
    }
}
