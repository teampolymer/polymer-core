package com.nmmoc7.polymercore.common.handler;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
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
        ItemStack heldItem = e.getPlayer().getHeldItem(e.getHand());
        if (heldItem.getItem() == Items.STICK) {
            PolymerCoreApi.getInstance().getMultiblockManager()
                .getDefinedMultiblock(new ResourceLocation(PolymerCoreApi.MOD_ID, "test_machine"))
                .ifPresent(it -> {
                    IAssembledMultiblock assemble = it.assemble(world, e.getPos());

                    if (assemble != null) {
                        e.getPlayer().sendMessage(new StringTextComponent("Multiblock Assembled"), Util.DUMMY_UUID);
                    }
                });


        }
    }
}
