package com.nmmoc7.polymercore.common.capability.chunk;

import com.nmmoc7.polymercore.api.capability.IChunkMultiblockStorage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.energy.IEnergyStorage;

public class CapabilityChunkMultiblockStorage {
    @CapabilityInject(IEnergyStorage.class)
    public static Capability<IChunkMultiblockStorage> MULTIBLOCK_STORAGE = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IChunkMultiblockStorage.class, new Capability.IStorage<IChunkMultiblockStorage>() {
                @Override
                public INBT writeNBT(Capability<IChunkMultiblockStorage> capability, IChunkMultiblockStorage instance, Direction side) {
                    CompoundNBT nbt = new CompoundNBT();
                    instance.getData().forEach((key, val) -> {
                        //TODO: 序列化
                    });
                    return nbt;
                }

                @Override
                public void readNBT(Capability<IChunkMultiblockStorage> capability, IChunkMultiblockStorage instance, Direction side, INBT nbt) {
                    if (!(nbt instanceof CompoundNBT)) {
                        return;
                    }
                    //TODO 反序列化

                }
            },
            () -> null);
    }
}
