package com.nmmoc7.polymercore.common.multiblock.assembled;

import com.nmmoc7.polymercore.PolymerCore;
import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.assembled.IFreeMultiblock;
import com.nmmoc7.polymercore.api.multiblock.assembled.IMultiblockAssembleRule;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
import com.nmmoc7.polymercore.common.capability.chunk.CapabilityChunkMultiblockStorage;
import com.nmmoc7.polymercore.common.world.FreeMultiblockWorldSavedData;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class FreeMultiblockImpl implements IFreeMultiblock {
    private static final Logger LOG = LogManager.getLogger();
    private UUID multiblockId;
    private IMultiblockAssembleRule assembleRule;
    private IDefinedMultiblock definedMultiblock;
    private Map<BlockPos, IMultiblockUnit> unitsMap;
    private Collection<ChunkPos> crossedChunks;
    private boolean initialized = false;

    @Override
    public UUID getMultiblockId() {
        return multiblockId;
    }

    public FreeMultiblockImpl() {
    }

    public FreeMultiblockImpl(UUID multiblockId, IMultiblockAssembleRule assembleRule, IDefinedMultiblock definedMultiblock) {
        this.multiblockId = multiblockId;
        this.assembleRule = assembleRule;
        this.definedMultiblock = definedMultiblock;

    }

    @Override
    public boolean initialize() {
        if (initialized) {
            return true;
        }
        Map<BlockPos, IMultiblockUnit> units = assembleRule.mapParts(getOriginalMultiblock());
        if (units == null || units.isEmpty()) {
            return false;
        }
        unitsMap = Collections.unmodifiableMap(units);
        Set<ChunkPos> posSet = units.keySet().stream()
            .map(ChunkPos::new)
            .collect(Collectors.toSet());
        if (posSet.isEmpty()) {
            return false;
        }
        crossedChunks = Collections.unmodifiableSet(
            posSet
        );
        initialized = true;
        return true;

    }


    @Override
    public void disassemble(World world) {
        if (initialize()) {
            for (ChunkPos crossedChunk : getCrossedChunks()) {
                world.getChunk(crossedChunk.x, crossedChunk.z).getCapability(CapabilityChunkMultiblockStorage.MULTIBLOCK_STORAGE)
                    .ifPresent(it -> it.removeMultiblock(getMultiblockId()));
            }
        }
        FreeMultiblockWorldSavedData.get(world).removeAssembledMultiblock(multiblockId);
        LOG.debug("The multiblock '{}' disassembled", multiblockId);
    }


    @Override
    public IDefinedMultiblock getOriginalMultiblock() {
        return definedMultiblock;
    }

    @Override
    public BlockPos getOffset() {
        return assembleRule.getOffset();
    }

    @Override
    public boolean isSymmetrical() {
        return assembleRule.isSymmetrical();
    }

    @Override
    public Rotation getRotation() {
        return assembleRule.getRotation();
    }

    @Override
    public Map<BlockPos, IMultiblockUnit> getUnits() {
        if (!initialized) {
            LOG.error("Multiblock {} not initialized", getMultiblockId());
            return Collections.emptyMap();
        }
        return unitsMap;
    }

    @Override
    public boolean validate(World world, boolean disassemble) {
        if (!initialize()) {
            return false;
        }
        boolean result = true;
        Map<BlockPos, IMultiblockUnit> parts = getUnits();
        for (Map.Entry<BlockPos, IMultiblockUnit> entry : parts.entrySet()) {
            BlockPos testPos = entry.getKey();
            BlockState block = world.getBlockState(testPos);
            if (entry.getValue().test(block)) {
                result = false;
                break;
            }
        }
        if (disassemble && !result) {
            disassemble(world);
        }
        return result;
    }

    @Override
    public IMultiblockAssembleRule getAssembleRule() {
        return assembleRule;
    }

    @Override
    public Collection<ChunkPos> getCrossedChunks() {
        if (!initialized) {
            LOG.error("Multiblock {} not initialized", getMultiblockId());
            return Collections.singleton(new ChunkPos(getOffset()));
        }
        return crossedChunks;
    }


    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putUUID("uuid", multiblockId);
        nbt.putString("define", definedMultiblock.getRegistryName().toString());
        nbt.put("rule", assembleRule.serializeNBT());
        nbt.putString("type", definedMultiblock.getType().getRegistryName().toString());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.multiblockId = nbt.getUUID("uuid");
        String define = nbt.getString("define");
        this.definedMultiblock = PolymerCoreApi.getInstance().getMultiblockManager().findById(new ResourceLocation(define))
            .orElseThrow(() -> new IllegalStateException(String.format("Could not get multiblock %s from NBT", define)));

        this.assembleRule = this.definedMultiblock.getType().createRuleFromNBT(nbt.getCompound("rule"));
    }
}
