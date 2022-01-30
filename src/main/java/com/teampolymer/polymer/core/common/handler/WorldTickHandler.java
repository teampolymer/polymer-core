package com.teampolymer.polymer.core.common.handler;

import com.teampolymer.polymer.core.api.PolymerCoreApi;
import com.teampolymer.polymer.core.api.multiblock.assembled.IFreeMultiblock;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;

@Mod.EventBusSubscriber(modid = PolymerCoreApi.MOD_ID)
public class WorldTickHandler {

    @SubscribeEvent
    public static void tickEnd(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.type != TickEvent.Type.WORLD || event.side != LogicalSide.SERVER) {
            return;
        }
//        World world = event.world;
//        Collection<IFreeMultiblock> multiblocks = FreeMultiblockWorldSavedData.get(world).getAssembledMultiblocks();
//
//        for (IFreeMultiblock multiblock : multiblocks) {
//            BlockPos offset = multiblock.getOffset();
//            Vector3i size = multiblock.getOriginalMultiblock().getSize();
//
//            if (world.isAreaLoaded(offset, Math.max(Math.max(size.getX(), size.getY()), size.getZ()))) {
//                tickMultiblock(world, multiblock);
//            }
//        }
    }


    private static void tickMultiblock(World world, IFreeMultiblock multiblock) {

    }

    private static void tickMachine() {

    }
}
