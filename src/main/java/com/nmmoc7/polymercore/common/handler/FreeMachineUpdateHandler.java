package com.nmmoc7.polymercore.common.handler;

import com.nmmoc7.polymercore.PolymerCore;
import com.nmmoc7.polymercore.api.capability.IChunkMultiblockStorage;
import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.common.capability.chunk.CapabilityChunkMultiblockStorage;
import com.nmmoc7.polymercore.common.capability.chunk.ChunkMultiblockCapabilityProvider;
import com.nmmoc7.polymercore.common.world.FreeMultiblockWorldSavedData;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = PolymerCore.MOD_ID)
public class FreeMachineUpdateHandler {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onBlockUpdate(BlockEvent.NeighborNotifyEvent event) {
        if (!(event.getWorld() instanceof ServerWorld)) {
            return;
        }
        if (event.getNotifiedSides().size() < 6) {
            return;
        }
        World world = (World) event.getWorld();
        BlockPos pos = event.getPos().toImmutable();
        if (world.isRemote) {
            return;
        }
        IChunk chunk = world.getChunk(pos);
        if (!(chunk instanceof ICapabilityProvider)) {
            return;
        }
        LazyOptional<IChunkMultiblockStorage> capability = ((ICapabilityProvider) chunk).getCapability(CapabilityChunkMultiblockStorage.MULTIBLOCK_STORAGE);
        capability.ifPresent(it -> {
            Tuple<UUID, IMultiblockPart> part = it.getMultiblockPart(pos);
            if (part == null) {
                return;
            }
            boolean test = part.getB().test(world.getBlockState(pos));
            if (!test) {
                IAssembledMultiblock assembledMultiblock = FreeMultiblockWorldSavedData.get(world).getAssembledMultiblock(part.getA());
                if (assembledMultiblock == null) {
                    PolymerCore.LOG.warn("The multiblock '{}' 's block in {} is invalid!", part.getA(), pos);
                    it.removeMultiblock(part.getA());
                    return;
                }
                //解除组装
                assembledMultiblock.disassemble();
            }
        });

    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<?> event) {
        if (!(event.getObject() instanceof Chunk)) {
            return;
        }
        Chunk chunk = (Chunk) event.getObject();
        ChunkMultiblockCapabilityProvider provider = new ChunkMultiblockCapabilityProvider(() -> chunk);
        event.addCapability(ChunkMultiblockCapabilityProvider.CAPABILITY_PROVIDER_CHUNK_MULTIBLOCK, provider);
        event.addListener(provider::invalidate);
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load load) {
        IChunk chunk = load.getChunk();
        IWorld world = load.getWorld();
        if (chunk instanceof ICapabilityProvider && world instanceof ServerWorld) {
            ((ICapabilityProvider) chunk).getCapability(CapabilityChunkMultiblockStorage.MULTIBLOCK_STORAGE)
                .ifPresent(it -> it.initialize((World) world));
        }
    }

    @SubscribeEvent
    public static void onChunkUnLoad(ChunkEvent.Unload unload) {
        IChunk chunk = unload.getChunk();
        if (chunk instanceof ICapabilityProvider) {
            ((ICapabilityProvider) chunk).getCapability(CapabilityChunkMultiblockStorage.MULTIBLOCK_STORAGE)
                .ifPresent(IChunkMultiblockStorage::invalidate);
        }
    }
}
