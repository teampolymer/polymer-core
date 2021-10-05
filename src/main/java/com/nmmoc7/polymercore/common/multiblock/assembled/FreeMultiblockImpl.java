package com.nmmoc7.polymercore.common.multiblock.assembled;

import com.nmmoc7.polymercore.PolymerCore;
import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.assembled.IFreeMultiblock;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.util.PositionUtils;
import com.nmmoc7.polymercore.common.capability.chunk.CapabilityChunkMultiblockStorage;
import com.nmmoc7.polymercore.common.registry.MultiblockManagerImpl;
import com.nmmoc7.polymercore.common.world.FreeMultiblockWorldSavedData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Lazy;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class FreeMultiblockImpl implements IFreeMultiblock {
    private UUID multiblockId;
    private BlockPos offset;
    private boolean isSymmetrical;
    private Rotation rotation;
    private World world;

    private IDefinedMultiblock definedMultiblock;
    private final Lazy<Map<BlockPos, IMultiblockPart>> partMap = Lazy.concurrentOf(() -> {
        Map<Vector3i, IMultiblockPart> parts = getOriginalMultiblock().getParts();
        Map<BlockPos, IMultiblockPart> result = new HashMap<>();
        for (Map.Entry<Vector3i, IMultiblockPart> entry : parts.entrySet()) {
            BlockPos pos = PositionUtils.applyModifies(entry.getKey(), offset, rotation, isSymmetrical);
            result.put(pos, entry.getValue());
        }
        return result;
    });
    private final Lazy<Collection<ChunkPos>> crossedChunks = Lazy.concurrentOf(() ->
        partMap.get().keySet().stream().map(ChunkPos::new).collect(Collectors.toSet())
    );

    @Override
    public UUID getMultiblockId() {
        return multiblockId;
    }

    public FreeMultiblockImpl() {
    }

    public FreeMultiblockImpl(UUID multiblockId, BlockPos offset, boolean isSymmetrical, Rotation rotation, IDefinedMultiblock definedMultiblock) {
        this.multiblockId = multiblockId;
        this.offset = offset;
        this.isSymmetrical = isSymmetrical;
        this.rotation = rotation;
        this.definedMultiblock = definedMultiblock;

    }

    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public void disassemble() {
        for (ChunkPos crossedChunk : getCrossedChunks()) {
            world.getChunk(crossedChunk.x, crossedChunk.z).getCapability(CapabilityChunkMultiblockStorage.MULTIBLOCK_STORAGE)
                .ifPresent(it -> it.removeMultiblock(getMultiblockId()));
        }
        FreeMultiblockWorldSavedData.get(world).removeAssembledMultiblock(multiblockId);
        PolymerCore.LOG.debug("The multiblock '{}' disassembled", multiblockId);
    }

    @Override
    public IDefinedMultiblock getOriginalMultiblock() {
        return definedMultiblock;
    }

    @Override
    public BlockPos getOffset() {
        return offset;
    }

    @Override
    public boolean isSymmetrical() {
        return isSymmetrical;
    }

    @Override
    public Rotation getRotation() {
        return rotation;
    }

    @Override
    public Map<BlockPos, IMultiblockPart> getParts() {
        return partMap.get();
    }

    @Override
    public Collection<ChunkPos> getCrossedChunks() {
        return crossedChunks.get();
    }


    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putUniqueId("uuid", multiblockId);
        nbt.put("off", NBTUtil.writeBlockPos(offset));
        nbt.putBoolean("symm", isSymmetrical);
        nbt.putByte("rotation", (byte) rotation.ordinal());
        nbt.putString("define", definedMultiblock.getRegistryName().toString());
        nbt.putString("type", definedMultiblock.getType().getRegistryName().toString());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.multiblockId = nbt.getUniqueId("uuid");
        this.offset = NBTUtil.readBlockPos(nbt.getCompound("off"));
        this.isSymmetrical = nbt.getBoolean("symm");
        this.rotation = Rotation.values()[nbt.getByte("rotation")];
        String define = nbt.getString("define");
        this.definedMultiblock = PolymerCoreApi.getInstance().getMultiblockManager().getDefinedMultiblock(new ResourceLocation(define))
            .orElseThrow(() -> new IllegalStateException(String.format("Could not get multiblock %s from NBT", define)));

    }
}
