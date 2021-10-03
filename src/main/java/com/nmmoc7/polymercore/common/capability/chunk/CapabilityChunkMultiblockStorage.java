package com.nmmoc7.polymercore.common.capability.chunk;

import com.nmmoc7.polymercore.api.capability.IChunkMultiblockStorage;
import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import com.nmmoc7.polymercore.api.util.MultiblockUtils;
import net.minecraft.nbt.*;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CapabilityChunkMultiblockStorage {
    @CapabilityInject(IEnergyStorage.class)
    public static Capability<IChunkMultiblockStorage> MULTIBLOCK_STORAGE = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IChunkMultiblockStorage.class, new Capability.IStorage<IChunkMultiblockStorage>() {
                @Override
                public INBT writeNBT(Capability<IChunkMultiblockStorage> capability, IChunkMultiblockStorage instance, Direction side) {
                    ListNBT nbt = new ListNBT();
                    for (UUID uuid : instance.getContainingMultiblocks()) {
                        //可恶啊，这句居然没有mcp
                        nbt.add(NBTUtil.func_240626_a_(uuid));
                    }
                    return nbt;
                }

                @Override
                public void readNBT(Capability<IChunkMultiblockStorage> capability, IChunkMultiblockStorage instance, Direction side, INBT nbt) {
                    if (!(nbt instanceof ListNBT)) {
                        return;
                    }
                    List<UUID> collect = ((ListNBT) nbt).stream().map(NBTUtil::readUniqueId).collect(Collectors.toList());
                    instance.setContainingMultiblocks(collect);
                }
            },
            () -> null);
    }
}
