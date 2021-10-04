package com.nmmoc7.polymercore.common.network;

import com.nmmoc7.polymercore.PolymerCore;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModNetworking {
    public static SimpleChannel INSTANCE;
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(PolymerCore.MOD_ID, "default_networking"),
            () -> VERSION,
            (version) -> version.equals(VERSION),
            (version) -> version.equals(VERSION)
        );
        INSTANCE.messageBuilder(PacketMultiblockDebug.class, nextID())
            .encoder(PacketMultiblockDebug::toBytes)
            .decoder(PacketMultiblockDebug::new)
            .consumer(PacketMultiblockDebug::handler)
            .add();
    }
}
