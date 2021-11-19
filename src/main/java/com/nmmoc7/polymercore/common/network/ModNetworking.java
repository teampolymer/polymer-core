package com.nmmoc7.polymercore.common.network;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
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
            new ResourceLocation(PolymerCoreApi.MOD_ID, "default_networking"),
            () -> VERSION,
            (version) -> version.equals(VERSION),
            (version) -> version.equals(VERSION)
        );
        INSTANCE.messageBuilder(PacketMultiblockDebug.class, nextID())
            .encoder(PacketMultiblockDebug::toBytes)
            .decoder(PacketMultiblockDebug::new)
            .consumer(PacketMultiblockDebug::handler)
            .add();

        INSTANCE.messageBuilder(PacketLocateHandlerSync.class, nextID())
            .encoder(PacketLocateHandlerSync::toBytes)
            .decoder(PacketLocateHandlerSync::new)
            .consumer(PacketLocateHandlerSync::handler)
            .add();

        INSTANCE.messageBuilder(PacketAssembleMultiblock.class, nextID())
            .encoder(PacketAssembleMultiblock::toBytes)
            .decoder(PacketAssembleMultiblock::new)
            .consumer(PacketAssembleMultiblock::handler)
            .add();
    }
}
