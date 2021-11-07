package com.nmmoc7.polymercore.common;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.common.item.Hammer;
import com.nmmoc7.polymercore.common.item.TestBlueprintItem;
import com.nmmoc7.polymercore.common.registry.KeysRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterHandler {
    public static final Hammer HAMMER = new Hammer();
    public static final TestBlueprintItem TEST_BLUEPRINT_ITEM = new TestBlueprintItem();


    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
            HAMMER.setRegistryName(new ResourceLocation(PolymerCoreApi.MOD_ID, "hammer")),
            TEST_BLUEPRINT_ITEM.setRegistryName(new ResourceLocation(PolymerCoreApi.MOD_ID, "test_blueprint"))
        );
    }
}

