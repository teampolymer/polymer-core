package com.teampolymer.polymer.core.common.network;

import com.teampolymer.polymer.core.api.PolymerCoreApi;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.FMLHandshakeHandler;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModNetworking {
    public static SimpleChannel INSTANCE;
    public static final String VERSION = "1.0";
    private static int ID = 0;
    private static final Logger LOG = LogManager.getLogger();

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

        INSTANCE.messageBuilder(LoginReplyPacket.class, nextID(), NetworkDirection.LOGIN_TO_SERVER)
            .loginIndex(LoginPacket::getLoginIndex, LoginPacket::setLoginIndex)
            .encoder(LoginReplyPacket::toBytes)
            .decoder(LoginReplyPacket::new)
            .consumer(FMLHandshakeHandler.indexFirst(((h, packet, ctx) -> {
                LOG.debug("Received client login reply {}", packet.getLoginIndex());
                ctx.get().setPacketHandled(true);
            })))
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
