package com.nmmoc7.polymercore.common.handler;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.api.multiblock.assembled.IFreeMultiblock;
import com.nmmoc7.polymercore.common.world.FreeMultiblockWorldSavedData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
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
        World world = event.world;
        Collection<IFreeMultiblock> multiblocks = FreeMultiblockWorldSavedData.get(world).getAssembledMultiblocks();

        for (IFreeMultiblock multiblock : multiblocks) {
            BlockPos offset = multiblock.getOffset();
            Vector3i size = multiblock.getOriginalMultiblock().getSize();

            if (world.isAreaLoaded(offset, Math.max(Math.max(size.getX(), size.getY()), size.getZ()))) {
                tickMultiblock(world, multiblock);
            }
        }
    }


    private static void tickMultiblock(World world, IFreeMultiblock multiblock) {

    }

    private static void tickMachine() {

    }
}
