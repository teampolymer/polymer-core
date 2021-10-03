package com.nmmoc7.polymercore.common.world;

import com.nmmoc7.polymercore.PolymerCore;
import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import com.nmmoc7.polymercore.api.util.MultiblockUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FreeMultiblockWorldSavedData extends WorldSavedData {
    private static final String NAME = "PolymerCoreFreeMultiblock";

    public FreeMultiblockWorldSavedData() {
        super(NAME);
        this.assembledMultiblockMap = new ConcurrentHashMap<>();
    }

    private final ConcurrentHashMap<UUID, IAssembledMultiblock> assembledMultiblockMap;

    public IAssembledMultiblock getAssembledMultiblock(UUID multiblockId) {
        return assembledMultiblockMap.get(multiblockId);
    }

    public void addAssembledMultiblock(IAssembledMultiblock multiblock) {
        IAssembledMultiblock result = assembledMultiblockMap.put(multiblock.getMultiblockId(), multiblock);
        if (result != null) {
            PolymerCore.LOG.error("Attempting to add an multiblock with existing id: {}", multiblock.getMultiblockId());
        }
        markDirty();
    }

    public void removeAssembledMultiblock(UUID multiblockId) {
        assembledMultiblockMap.remove(multiblockId);
        markDirty();
    }

    @Override
    public void read(CompoundNBT nbt) {
        assembledMultiblockMap.clear();
        ListNBT multiblocks = nbt.getList("assembled_multiblocks", 10);
        for (INBT multiblock : multiblocks) {
            IAssembledMultiblock assembledMultiblock = MultiblockUtils.deserializeNBT(multiblock);
            if (assembledMultiblock == null) {
                continue;
            }
            assembledMultiblockMap.put(assembledMultiblock.getMultiblockId(), assembledMultiblock);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ListNBT listNBT = new ListNBT();
        for (IAssembledMultiblock value : assembledMultiblockMap.values()) {
            listNBT.add(value.serializeNBT());
        }
        compound.put("assembled_multiblocks", listNBT);
        return compound;
    }

    public static FreeMultiblockWorldSavedData get(World worldIn) {
        if (!(worldIn instanceof ServerWorld)) {
            throw new RuntimeException("Attempted to get the data from a client world. This is wrong.");
        }
        ServerWorld world = (ServerWorld) worldIn;
        DimensionSavedDataManager storage = world.getSavedData();
        return storage.getOrCreate(FreeMultiblockWorldSavedData::new, NAME);
    }
}
