package com.nmmoc7.polymercore.common.network;

import com.nmmoc7.polymercore.api.capability.IMultiblockLocateHandler;
import com.nmmoc7.polymercore.common.capability.blueprint.CapabilityMultiblockItem;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketLocateHandlerSync {
    private final int slot;
    private final boolean anchored;
    private final BlockPos offset;
    private final Rotation rotation;
    private final boolean flipped;

    public PacketLocateHandlerSync(int slot, IMultiblockLocateHandler locateHandler) {
        this.slot = slot;
        this.anchored = locateHandler.isAnchored();
        this.offset = locateHandler.getOffset();
        this.rotation = locateHandler.getRotation();
        this.flipped = locateHandler.isFlipped();
    }

    public PacketLocateHandlerSync(PacketBuffer buffer) {
        this.slot = buffer.readVarInt();
        this.anchored = buffer.readBoolean();
        this.offset = buffer.readBlockPos();
        this.rotation = buffer.readEnum(Rotation.class);
        this.flipped = buffer.readBoolean();
    }


    public void toBytes(PacketBuffer buffer) {
        buffer.writeVarInt(slot);
        buffer.writeBoolean(anchored);
        buffer.writeBlockPos(offset);
        buffer.writeEnum(rotation);
        buffer.writeBoolean(flipped);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player == null)
                    return;
                ItemStack stack;
                if (slot == -1) {
                    stack = player.getOffhandItem();
                } else {
                    stack = player.inventory.getItem(slot);
                }

                stack.getCapability(CapabilityMultiblockItem.MULTIBLOCK_LOCATE_HANDLER).ifPresent(it -> {
                    it.setAnchored(anchored);
                    it.setOffset(offset);
                    it.setRotation(rotation);
                    it.setFlipped(flipped);
                });

            });
        }
        ctx.get().setPacketHandled(true);
    }
}
