package com.nmmoc7.polymercore.common.handler;

import com.nmmoc7.polymercore.PolymerCore;
import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import com.nmmoc7.polymercore.common.world.FreeMultiblockWorldSavedData;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = PolymerCore.MOD_ID)
public class PlayerHandler {
    @SubscribeEvent
    public static void onPlayerRightClick(PlayerInteractEvent.RightClickBlock e) {
        ItemStack heldItem = e.getPlayer().getHeldItem(e.getHand());
        if (heldItem.getItem() == Items.STICK) {
            World world = e.getPlayer().world;
            IAssembledMultiblock assemble = MultiblockRegisterHandler.TestMachine.get().assemble(world, e.getPos());
            if(assemble != null) {
                e.getPlayer().sendMessage(new StringTextComponent("Multiblock Assembled"), Util.DUMMY_UUID);
            }
        }
    }
}
