package com.nmmoc7.polymercore.common.registry;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.common.event.MultiblockReloadListener;
import com.nmmoc7.polymercore.common.handler.MultiblockRegisterHandler;
import com.nmmoc7.polymercore.common.multiblock.builder.DefaultCharMarkedMultiblockBuilder;
import com.nmmoc7.polymercore.common.multiblock.unit.UnitSpecifiedBlock;
import com.nmmoc7.polymercore.common.network.ModNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = PolymerCoreApi.MOD_ID)
public class ReloadListenerHandler {
    @SubscribeEvent
    public static void onAddReloadListener(AddReloadListenerEvent e) {
        e.addListener(new MultiblockReloadListener());
    }

}
