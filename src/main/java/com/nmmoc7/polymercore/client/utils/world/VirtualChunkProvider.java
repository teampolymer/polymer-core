package com.nmmoc7.polymercore.client.utils.world;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.lighting.WorldLightManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VirtualChunkProvider extends AbstractChunkProvider {

    private final VirtualWorld world;
    private final Long2ObjectMap<VirtualChunk> loadedChunks = new Long2ObjectOpenHashMap<>(8192);
    private final VirtualChunk blankChunk;
    private final WorldLightManager lightManager;

    public VirtualChunkProvider(VirtualWorld world) {
        this.world = world;
        this.blankChunk = new VirtualChunk(world, new ChunkPos(0, 0));
        this.lightManager = new FakeLightManager();
    }

    public void loadChunk(int chunkX, int chunkZ)
    {
        VirtualChunk chunk = new VirtualChunk(this.world, new ChunkPos(chunkX, chunkZ));
        this.loadedChunks.put(ChunkPos.asLong(chunkX, chunkZ), chunk);
    }

    public void unloadChunk(int chunkX, int chunkZ)
    {
        VirtualChunk chunk = this.loadedChunks.remove(ChunkPos.asLong(chunkX, chunkZ));

        if (chunk != null)
        {
            this.world.onChunkUnloaded(chunk);

            for (ClassInheritanceMultiMap<Entity> list : chunk.getEntitySections())
            {
                for (Entity entity : list.find(Entity.class))
                {
                    this.world.removeEntityFromWorld(entity.getId());
                }
            }
        }
    }


    @Nullable
    @Override
    public VirtualChunk getChunk(int chunkX, int chunkZ, @NotNull ChunkStatus requiredStatus, boolean nonnull) {
        VirtualChunk chunk = this.getChunkNow(chunkX, chunkZ);
        return chunk == null && nonnull ? this.blankChunk : chunk;
    }


    @Nullable
    @Override
    public VirtualChunk getChunkNow(int chunkX, int chunkZ) {
        VirtualChunk chunk = this.loadedChunks.get(ChunkPos.asLong(chunkX, chunkZ));
        return chunk == null ? this.blankChunk : chunk;
    }

    public int loadedChunkSize()
    {
        return this.loadedChunks.size();
    }


    @Override
    public @NotNull String gatherStats() {
        return "Virtual Chunk Cache: " + this.loadedChunkSize();
    }

    @Override
    public @NotNull WorldLightManager getLightEngine() {
        return lightManager;
    }

    @Override
    public @NotNull VirtualWorld getLevel() {
        return this.world;
    }

    @Override
    public boolean isEntityTickingChunk(ChunkPos pos) {
        return this.loadedChunks.containsKey(pos.toLong());
    }

    @Override
    public boolean hasChunk(int x, int z) {
        return this.loadedChunks.containsKey(ChunkPos.asLong(x, z));
    }

    @Override
    public boolean isEntityTickingChunk(Entity entityIn) {
        return this.hasChunk(MathHelper.floor(entityIn.getX()) >> 4, MathHelper.floor(entityIn.getZ()) >> 4);
    }
}
