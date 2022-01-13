package com.nmmoc7.polymercore.common.handler;

import com.nmmoc7.polymercore.PolymerCore;
import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.api.capability.IChunkMultiblockStorage;
import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
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
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = PolymerCoreApi.MOD_ID)
public class FreeMachineUpdateHandler {
    private static final Logger LOG = LogManager.getLogger();

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
        BlockPos pos = event.getPos().immutable();
        if (world.isClientSide) {
            return;
        }
        Optional<IChunkMultiblockStorage> capability = CapabilityChunkMultiblockStorage.getCapability(world, pos);
        if (!capability.isPresent()) {
            return;
        }
        IChunkMultiblockStorage it = capability.get();
        Tuple<UUID, IMultiblockUnit> part = it.getMultiblockPart(pos);
        if (part == null) {
            return;
        }
        boolean test = part.getB().test(world.getBlockState(pos));
        if (!test) {
            IAssembledMultiblock assembledMultiblock = FreeMultiblockWorldSavedData.get(world).getAssembledMultiblock(part.getA());
            if (assembledMultiblock == null) {
                LOG.warn("The multiblock '{}' 's block in {} is invalid!", part.getA(), pos);
                it.removeMultiblock(part.getA());
                return;
            }
            //解除组装
            assembledMultiblock.disassemble(world);
        }

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
        if (world.isClientSide()) {
            return;
        }
        Set<UUID> multiblocks = new HashSet<>();
        if (chunk instanceof ICapabilityProvider) {
            ((ICapabilityProvider) chunk).getCapability(CapabilityChunkMultiblockStorage.MULTIBLOCK_STORAGE)
                .ifPresent(it -> {
                    it.initialize((World) world);
                    multiblocks.addAll(it.getContainingMultiblocks());
                });
        }
        FreeMultiblockWorldSavedData.get((World) world).validateMultiblocksInChunk(chunk.getPos(), multiblocks);

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
