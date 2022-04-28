package com.teampolymer.polymer.core.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.teampolymer.polymer.core.client.shader.NormalShaderManager;
import com.teampolymer.polymer.core.client.shader.VanillaShaderManager;
import com.teampolymer.polymer.core.common.registry.KeysRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class PolymerClient {
    public static void onCtorClient(IEventBus modBus, IEventBus forgeBus) {

        modBus.addListener(PolymerClient::clientInit);
        modBus.addListener(PolymerClient::onResourceReload);


        IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        if (resourceManager instanceof IReloadableResourceManager) {
            ((IReloadableResourceManager) resourceManager).registerReloadListener((IResourceManagerReloadListener) it ->
            {
                VanillaShaderManager.init(it);
                NormalShaderManager.init(it);
            });
        }
    }

    public static void clientInit(final FMLClientSetupEvent event) {
        KeysRegistry.init();
    }


    public static void onResourceReload(ModelRegistryEvent event) {
    }

}
