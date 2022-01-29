package com.teampolymer.polymer.core.common.network;

import com.teampolymer.polymer.core.api.multiblock.IAssembledMultiblock;
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

            });
        }
        ctx.get().setPacketHandled(true);
    }
}
