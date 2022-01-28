package com.nmmoc7.polymercore.common.network;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.network.FMLHandshakeHandler;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

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

        INSTANCE.messageBuilder(TestLoginPacket.class, nextID(), NetworkDirection.LOGIN_TO_CLIENT)
            .loginIndex(LoginPacket::getLoginIndex, LoginPacket::setLoginIndex)
            .encoder(TestLoginPacket::toBytes)
            .decoder(TestLoginPacket::new)
            .consumer((packet, ctx) -> {
                LOG.debug("Received server login packet {}", packet.getLoginIndex());
                packet.handler(ctx);
                INSTANCE.reply(new LoginReplyPacket(), ctx.get());
            })
            .markAsLoginPacket()
            .add();

        INSTANCE.messageBuilder(PacketChunkAura.class, nextID(), NetworkDirection.PLAY_TO_CLIENT)
            .encoder(PacketChunkAura::toBytes)
            .decoder(PacketChunkAura::new)
            .consumer(PacketChunkAura::handler)
            .add();

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
