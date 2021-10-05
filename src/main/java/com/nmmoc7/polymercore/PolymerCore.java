package com.nmmoc7.polymercore;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.common.capability.chunk.CapabilityChunkMultiblockStorage;
import com.nmmoc7.polymercore.common.handler.MultiblockRegisterHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PolymerCoreApi.MOD_ID)
public class PolymerCore {

    public static final Logger LOG = LogManager.getLogger();

    public PolymerCore() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        MultiblockRegisterHandler.MULTIBLOCK_TYPES.register(modBus);

        modBus.addListener(this::preInit);
    }


    public void preInit(FMLCommonSetupEvent event) {
        event.enqueueWork(CapabilityChunkMultiblockStorage::register);
    }

}