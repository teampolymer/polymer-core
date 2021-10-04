package com.nmmoc7.polymercore.common.handler;

import com.nmmoc7.polymercore.PolymerCore;
import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.util.PositionUtils;
import com.nmmoc7.polymercore.client.handler.MultiblockProjectionHandler;
import com.nmmoc7.polymercore.common.registry.MultiblockManagerImpl;
import com.nmmoc7.polymercore.common.world.FreeMultiblockWorldSavedData;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = PolymerCore.MOD_ID)
public class PlayerHandler {
    @SubscribeEvent
    public static void onPlayerRightClick(PlayerInteractEvent.RightClickBlock e) {
        World world = e.getPlayer().world;
        if (world.isRemote) {
            return;
        }
        ItemStack heldItem = e.getPlayer().getHeldItem(e.getHand());
        if (heldItem.getItem() == Items.STICK) {
            MultiblockManagerImpl.INSTANCE.get()
                .getDefinedMultiblock(new ResourceLocation(PolymerCore.MOD_ID, "test_machine"))
                .ifPresent(it -> {
                    IAssembledMultiblock assemble = it.assemble(world, e.getPos());

                    if (assemble != null) {
                        e.getPlayer().sendMessage(new StringTextComponent("Multiblock Assembled"), Util.DUMMY_UUID);
                    } else {
                        MultiblockProjectionHandler.setMultiblock(it);
                        MultiblockProjectionHandler.setIsProjecting(true);
                        MultiblockProjectionHandler.setTargetPos(e.getPos());
                    }
                });


        }
    }
}
