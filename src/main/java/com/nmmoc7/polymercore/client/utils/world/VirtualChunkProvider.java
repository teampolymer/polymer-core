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

            for (ClassInheritanceMultiMap<Entity> list : chunk.getEntityLists())
            {
                for (Entity entity : list.getByClass(Entity.class))
                {
                    this.world.removeEntityFromWorld(entity.getEntityId());
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
    public @NotNull String makeString() {
        return "Virtual Chunk Cache: " + this.loadedChunkSize();
    }

    @Override
    public @NotNull WorldLightManager getLightManager() {
        return lightManager;
    }

    @Override
    public @NotNull VirtualWorld getWorld() {
        return this.world;
    }

    @Override
    public boolean isChunkLoaded(ChunkPos pos) {
        return this.loadedChunks.containsKey(pos.asLong());
    }

    @Override
    public boolean chunkExists(int x, int z) {
        return this.loadedChunks.containsKey(ChunkPos.asLong(x, z));
    }

    @Override
    public boolean isChunkLoaded(Entity entityIn) {
        return this.chunkExists(MathHelper.floor(entityIn.getPosX()) >> 4, MathHelper.floor(entityIn.getPosZ()) >> 4);
    }
}
