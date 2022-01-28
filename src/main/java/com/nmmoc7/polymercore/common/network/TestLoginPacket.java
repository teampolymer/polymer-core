package com.nmmoc7.polymercore.common.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class TestLoginPacket extends LoginPacket {

    public TestLoginPacket(PacketBuffer buffer) {
    }

    public TestLoginPacket() {

    }

    public void toBytes(PacketBuffer buf) {

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
