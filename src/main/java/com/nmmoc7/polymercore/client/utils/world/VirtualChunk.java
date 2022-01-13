package com.nmmoc7.polymercore.client.utils.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeContainer;
import net.minecraft.world.biome.BiomeRegistry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.gen.Heightmap;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class VirtualChunk extends Chunk {
    private static final BlockState AIR = Blocks.AIR.defaultBlockState();

    private final long timeCreated;
    private boolean isEmpty = true;

    public VirtualChunk(VirtualWorld world, ChunkPos pos) {
        super(world, pos, new BiomeContainer(world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY),
            Util.make(new Biome[BiomeContainer.BIOMES_SIZE],
                (biomes) -> Arrays.fill(biomes, BiomeRegistry.THE_VOID))));
        this.timeCreated = world.getGameTime();
    }

    @Override
    public @NotNull BlockState getBlockState(BlockPos pos) {
        int x = pos.getX() & 0xF;
        int y = pos.getY();
        int z = pos.getZ() & 0xF;
        int cy = y >> 4;

        ChunkSection[] sections = this.getSections();

        if (cy >= 0 && cy < sections.length) {
            ChunkSection chunkSection = sections[cy];

            if (!ChunkSection.isEmpty(chunkSection)) {
                return chunkSection.getBlockState(x, y & 0xF, z);
            }
        }

        return AIR;
    }

    @Override
    public BlockState setBlockState(BlockPos pos, BlockState state, boolean isMoving) {
        int x = pos.getX() & 15;
        int y = pos.getY();
        int z = pos.getZ() & 15;
        ChunkSection chunksection = this.getSections()[y >> 4];
        if (chunksection == EMPTY_SECTION) {
            if (state.isAir()) {
                return null;
            }

            chunksection = new ChunkSection(y >> 4 << 4);
            this.getSections()[y >> 4] = chunksection;
        }
        if (!state.isAir())
        {
            this.isEmpty = false;
        }

        BlockState oldState = chunksection.setBlockState(x, y & 15, z, state);
        if (oldState == state) {
            return null;
        } else {
            Block newBlock = state.getBlock();
            Block oldBlock = oldState.getBlock();
            this.getOrCreateHeightmapUnprimed(Heightmap.Type.MOTION_BLOCKING).update(x, y, z, state);
            this.getOrCreateHeightmapUnprimed(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES).update(x, y, z, state);
            this.getOrCreateHeightmapUnprimed(Heightmap.Type.OCEAN_FLOOR).update(x, y, z, state);
            this.getOrCreateHeightmapUnprimed(Heightmap.Type.WORLD_SURFACE).update(x, y, z, state);

            if ((oldBlock != newBlock || !state.hasTileEntity()) && oldState.hasTileEntity()) {
//                this.world.removeTileEntity(pos);
            }

            if (!chunksection.getBlockState(x, y & 15, z).is(newBlock)) {
                return null;
            } else {
                if (oldState.hasTileEntity()) {
                    TileEntity tileentity = this.getBlockEntity(pos, Chunk.CreateEntityType.CHECK);
                    if (tileentity != null) {
                        tileentity.clearCache();
                    }
                }

                if (state.hasTileEntity()) {
                    TileEntity te = this.getBlockEntity(pos, Chunk.CreateEntityType.CHECK);
                    if (te == null) {
                        te = state.createTileEntity(this.getLevel());
//                        this.world.setTileEntity(pos, te);
                    } else {
                        te.clearCache();
                    }
                }

                this.markUnsaved();
                return oldState;
            }
        }

    }


    public long getTimeCreated() {
        return this.timeCreated;
    }


    @Override
    public boolean isEmpty() {
        return this.isEmpty;
    }
}
