package com.nmmoc7.polymercore;

import com.nmmoc7.polymercore.common.blueprint.BlueprintRegisterHandler;
import com.nmmoc7.polymercore.common.item.TestItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PolymerCore.MOD_ID)
public class PolymerCore {
    public static final String MOD_ID = "polymer-core";

    public PolymerCore() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        BlueprintRegisterHandler.REGISTER.register(modBus);
        TestItem.ItemRegistry.ITEMS.register(modBus);
    }
}
