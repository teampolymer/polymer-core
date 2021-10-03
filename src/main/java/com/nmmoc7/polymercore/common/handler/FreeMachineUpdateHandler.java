package com.nmmoc7.polymercore.common.handler;

import com.nmmoc7.polymercore.PolymerCore;
import com.nmmoc7.polymercore.api.capability.IChunkMultiblockStorage;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.common.capability.chunk.CapabilityChunkMultiblockStorage;
import com.nmmoc7.polymercore.common.capability.chunk.ChunkMultiblockCapabilityProvider;
import com.nmmoc7.polymercore.core.event.BlockUpdateEvent;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PolymerCore.MOD_ID)
public class FreeMachineUpdateHandler {

    @SubscribeEvent
    public static void onBlockUpdate(BlockUpdateEvent event) {
        World world = event.getWorld();
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
            Tuple<BlockPos, IMultiblockPart> part = it.getMultiblockPart(pos);
            if (part == null) {
                return;
            }
            boolean test = part.getB().test(world.getBlockState(pos));
            if (!test) {
                TileEntity coreBlock = world.getTileEntity(part.getA());
                if (coreBlock == null) {
                    PolymerCore.LOG.warn("The core machine in {} of a block {} is invalid!", part.getA(), pos);
                    it.removeMachine(part.getA());
                    return;
                }
                //TODO: 这里需要处理机器解除组装
            }
        });

    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<?> event) {
        if (!(event.getObject() instanceof Chunk)) {
            return;
        }
        IChunk chunk = (IChunk) event.getObject();
        ChunkMultiblockCapabilityProvider provider = new ChunkMultiblockCapabilityProvider(() -> chunk);
        event.addCapability(ChunkMultiblockCapabilityProvider.CAPABILITY_PROVIDER_CHUNK_MULTIBLOCK, provider);

    }
}
