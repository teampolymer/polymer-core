package com.nmmoc7.polymercore.common.capability.chunk;

import com.nmmoc7.polymercore.PolymerCore;
import com.nmmoc7.polymercore.api.capability.IChunkMultiblockStorage;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class CapabilityChunkMultiblockStorage {

    private static final Logger LOG = LogManager.getLogger();

    @CapabilityInject(IChunkMultiblockStorage.class)
    public static Capability<IChunkMultiblockStorage> MULTIBLOCK_STORAGE = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IChunkMultiblockStorage.class, new Capability.IStorage<IChunkMultiblockStorage>() {
                @Override
                public INBT writeNBT(Capability<IChunkMultiblockStorage> capability, IChunkMultiblockStorage instance, Direction side) {
                    ListNBT nbt = new ListNBT();
                    for (UUID uuid : instance.getContainingMultiblocks()) {
                        //可恶啊，这句居然没有mcp
                        nbt.add(NBTUtil.createUUID(uuid));
                    }
                    if (LOG.isDebugEnabled() && nbt.size() > 0) {
                        LOG.debug("Saving {} machines in chunk {}", nbt.size(), instance.getChunk().getPos());
                    }
                    return nbt;
                }

                @Override
                public void readNBT(Capability<IChunkMultiblockStorage> capability, IChunkMultiblockStorage instance, Direction side, INBT nbt) {
                    if (!(nbt instanceof ListNBT)) {
                        return;
                    }
                    List<UUID> collect = ((ListNBT) nbt).stream().map(NBTUtil::loadUUID).collect(Collectors.toList());
                    instance.setContainingMultiblocks(collect);
                    if (LOG.isDebugEnabled() && collect.size() > 0) {
                        LOG.debug("Loading {} machines in chunk {}", collect.size(), instance.getChunk().getPos());
                    }
                }
            },
            () -> null);
    }

    public static Optional<IChunkMultiblockStorage> getCapability(World world, BlockPos pos) {
        IChunk chunk = world.getChunk(pos);
        if (!(chunk instanceof ICapabilityProvider)) {
            return Optional.empty();
        }
        LazyOptional<IChunkMultiblockStorage> capability = ((ICapabilityProvider) chunk).getCapability(CapabilityChunkMultiblockStorage.MULTIBLOCK_STORAGE);
        if (!capability.isPresent()) {
            return Optional.empty();
        }
        return capability.resolve();
    }

    public static Tuple<UUID, IMultiblockUnit> getMultiblockPart(World world, BlockPos pos) {

        IChunkMultiblockStorage storage = getCapability(world, pos).orElse(null);
        if (storage == null) {
            return null;
        }
        return storage.getMultiblockPart(pos);
    }
}
