package com.nmmoc7.polymercore;

import com.nmmoc7.polymercore.common.block.TestBlock;
import com.nmmoc7.polymercore.common.capability.chunk.CapabilityChunkMultiblockStorage;
import com.nmmoc7.polymercore.common.handler.MultiblockRegisterHandler;
import com.nmmoc7.polymercore.common.item.TestItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PolymerCore.MOD_ID)
public class PolymerCore {
    public static final String MOD_ID = "polymer-core";

    public static final Logger LOG = LogManager.getLogger();

    public PolymerCore() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        TestItem.ItemRegistry.ITEMS.register(modBus);
        TestBlock.BlockRegistry.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        MultiblockRegisterHandler.MULTIBLOCK_TYPES.register(modBus);
        MultiblockRegisterHandler.DEFINED_MULTIBLOCKS.register(modBus);

        modBus.addListener(this::preInit);
    }


    public void preInit(FMLCommonSetupEvent event) {
        event.enqueueWork(CapabilityChunkMultiblockStorage::register);
    }

}