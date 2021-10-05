package com.nmmoc7.polymercore.common.network;

import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import com.nmmoc7.polymercore.client.handler.MultiblockDebugHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class PacketMultiblockDebug {
    private final List<IAssembledMultiblock> multiblocks;

    public PacketMultiblockDebug(PacketBuffer buffer) {
        multiblocks = null;
    }

    public PacketMultiblockDebug(CompoundNBT message) {
        multiblocks = null;
    }

    public void toBytes(PacketBuffer buf) {

    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT){
            ctx.get().enqueueWork(() -> {
                MultiblockDebugHandler.isDebug = true;

            });
        }
        ctx.get().setPacketHandled(true);
    }
}
