package com.nmmoc7.polymercore.common.capability.chunk;

import com.nmmoc7.polymercore.PolymerCore;
import com.nmmoc7.polymercore.api.capability.IChunkMultiblockStorage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ChunkMultiblockCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundNBT> {
    public static final ResourceLocation CAPABILITY_PROVIDER_CHUNK_MULTIBLOCK = new ResourceLocation(PolymerCore.MOD_ID, "capability_provider_chunk_multiblock");

    public ChunkMultiblockCapabilityProvider(Supplier<IChunk> chunkSupplier) {
        this.CONTEXT = LazyOptional.of(() -> new ChunkMultiblockStorage(chunkSupplier.get()));
    }

    private final LazyOptional<IChunkMultiblockStorage> CONTEXT;

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return CapabilityChunkMultiblockStorage.MULTIBLOCK_STORAGE.orEmpty(cap, CONTEXT);
    }

    @Override
    public CompoundNBT serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }
}
