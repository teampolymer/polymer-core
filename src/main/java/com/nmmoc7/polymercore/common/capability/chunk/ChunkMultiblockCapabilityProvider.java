package com.nmmoc7.polymercore.common.capability.chunk;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.api.capability.IChunkMultiblockStorage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ChunkMultiblockCapabilityProvider implements ICapabilitySerializable<INBT> {
    public static final ResourceLocation CAPABILITY_PROVIDER_CHUNK_MULTIBLOCK = new ResourceLocation(PolymerCoreApi.MOD_ID, "capability_provider_chunk_multiblock");

    public ChunkMultiblockCapabilityProvider(Supplier<Chunk> chunkSupplier) {
        this.CONTEXT = LazyOptional.of(() -> new ChunkMultiblockStorage(chunkSupplier.get()));
    }

    private final LazyOptional<IChunkMultiblockStorage> CONTEXT;

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return CapabilityChunkMultiblockStorage.MULTIBLOCK_STORAGE.orEmpty(cap, CONTEXT);
    }

    public void invalidate() {
        CONTEXT.ifPresent(IChunkMultiblockStorage::invalidate);
    }

    @Override
    public INBT serializeNBT() {
        return CONTEXT.resolve().map(storage ->
            CapabilityChunkMultiblockStorage.MULTIBLOCK_STORAGE
                .getStorage().writeNBT(
                    CapabilityChunkMultiblockStorage.MULTIBLOCK_STORAGE,
                    storage,
                    null
                )
        ).orElse(new CompoundNBT());
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CONTEXT.ifPresent(it -> {
            CapabilityChunkMultiblockStorage.MULTIBLOCK_STORAGE
                .getStorage().readNBT(CapabilityChunkMultiblockStorage.MULTIBLOCK_STORAGE,
                    it, null, nbt);
        });
    }
}
