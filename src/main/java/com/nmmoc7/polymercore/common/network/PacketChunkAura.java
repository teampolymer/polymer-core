package com.nmmoc7.polymercore.common.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketChunkAura {
    private int chunkX;
    private int chunkZ;
    private double aura;

    public PacketChunkAura(int chunkX, int chunkZ, double aura) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.aura = aura;
    }

    public PacketChunkAura(PacketBuffer buf) {
        this.chunkX = buf.readInt();
        this.chunkZ = buf.readInt();
        this.aura = buf.readDouble();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(this.chunkX);
        buf.writeInt(this.chunkZ);
        buf.writeDouble(this.aura);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection() == NetworkDirection.LOGIN_TO_CLIENT) {
            ctx.get().enqueueWork(() -> {
                //TODO：xxx
            });
        }
        ctx.get().setPacketHandled(true);


    }
}
