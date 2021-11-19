package com.nmmoc7.polymercore.common.handler;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.api.capability.IMultiblockSupplier;
import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.common.capability.blueprint.CapabilityMultiblock;
import com.nmmoc7.polymercore.common.capability.chunk.ChunkMultiblockCapabilityProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PolymerCoreApi.MOD_ID)
public class PlayerHandler {
    @SubscribeEvent
    public static void onPlayerRightClick(PlayerInteractEvent.RightClickBlock e) {
        World world = e.getPlayer().world;
        if (world.isRemote) {
            return;
        }
    }
}
