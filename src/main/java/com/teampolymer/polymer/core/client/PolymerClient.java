package com.teampolymer.polymer.core.client;

import com.teampolymer.polymer.core.common.registry.KeysRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class PolymerClient {
    public static void onCtorClient(IEventBus modBus, IEventBus forgeBus) {

        modBus.addListener(PolymerClient::clientInit);
    }

    public static void clientInit(final FMLClientSetupEvent event) {
        KeysRegistry.init();
    }


}
