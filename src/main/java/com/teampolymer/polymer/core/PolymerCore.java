package com.teampolymer.polymer.core;

import com.teampolymer.polymer.core.api.PolymerCoreApi;
import com.teampolymer.polymer.core.api.manager.IWorldMultiblockManager;
import com.teampolymer.polymer.core.api.util.PolymerInternalConstants;
import com.teampolymer.polymer.core.client.PolymerClient;
import com.teampolymer.polymer.core.common.PolymerCoreApiImpl;
import com.teampolymer.polymer.core.common.block.ModBlocks;
import com.teampolymer.polymer.core.common.capability.blueprint.CapabilityMultiblockItem;
import com.teampolymer.polymer.core.common.item.ModItems;
import com.teampolymer.polymer.core.common.network.ModNetworking;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PolymerCoreApi.MOD_ID)
public class PolymerCore {

    private static final Logger LOG = LogManager.getLogger();

    public PolymerCore() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        ModItems.REGISTER.register(modBus);
        ModBlocks.REGISTER.register(modBus);

        modBus.addListener(this::preInit);


        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
            () -> () -> PolymerClient.onCtorClient(modBus, forgeBus));
    }


    private void processIMC(final InterModProcessEvent event) {
        event.getIMCStream().forEach(it -> {
            if (PolymerInternalConstants.IMC_WORLD_MULTIBLOCK_MANAGER.equals(it.getMethod())) {
                IWorldMultiblockManager manager = it.<IWorldMultiblockManager>getMessageSupplier().get();
                ((PolymerCoreApiImpl) PolymerCoreApi.getInstance()).setWorldMultiblockManager(manager);
            }
        });
    }

    public void preInit(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModNetworking.registerMessage();
            registerCapabilities();
        });
    }


    public void registerCapabilities() {
        CapabilityMultiblockItem.register();
    }

}