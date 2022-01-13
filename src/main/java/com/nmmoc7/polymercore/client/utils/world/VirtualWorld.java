package com.nmmoc7.polymercore.client.utils.world;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.profiler.IProfiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.tags.ITagCollectionSupplier;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeRegistry;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.storage.ISpawnWorldInfo;
import net.minecraft.world.storage.MapData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class VirtualWorld extends World {
    private final Minecraft mc;
    private final VirtualChunkProvider chunkProvider;
    private final VirtualWorldRender worldRenderer;
    private final Int2ObjectOpenHashMap<Entity> entitiesById = new Int2ObjectOpenHashMap<>();
    private int nextEntityId;

    protected VirtualWorld(ISpawnWorldInfo mutableWorldProperties, DimensionType dimensionType, Supplier<IProfiler> profiler) {
        super(mutableWorldProperties, null, dimensionType, profiler, true, true, 0L);

        this.mc = Minecraft.getInstance();
        this.worldRenderer = new VirtualWorldRender();
        this.chunkProvider = new VirtualChunkProvider(this);
    }


    @Override
    public @NotNull AbstractChunkProvider getChunkProvider() {
        return chunkProvider;
    }

    @Override
    public Chunk getChunk(int chunkX, int chunkZ, ChunkStatus status, boolean required) {
        return this.chunkProvider.getChunk(chunkX, chunkZ, status, required);
    }


    public void onChunkUnloaded(Chunk chunkIn) {
        this.tileEntitiesToBeRemoved.addAll(chunkIn.getTileEntityMap().values());
//        this.chunkProvider.getLightManager().enableLightSources(chunkIn.getPos(), false);
    }

    @Override
    public boolean setBlockState(BlockPos pos, BlockState state, int flags, int recursionLeft) {
        if (isOutsideBuildHeight(pos)) {
            return false;
        }
        Chunk chunk = this.getChunkAt(pos);
        pos = pos.toImmutable(); // Forge - prevent mutable BlockPos leaks
        BlockState oldState = chunk.setBlockState(pos, state, false);
        return oldState != null;
    }

    public boolean spawnEntity(Entity entityToSpawn) {
        return this.addEntityImpl(nextEntityId++, entityToSpawn);
    }

    private boolean addEntityImpl(int entityIdIn, Entity entityToSpawn) {
        int cx = MathHelper.floor(entityToSpawn.getPosX() / 16.0D);
        int cz = MathHelper.floor(entityToSpawn.getPosX() / 16.0D);

        if (!this.chunkProvider.chunkExists(cx, cz)) {
            return false;
        } else {
            entityToSpawn.setEntityId(entityIdIn);

            this.removeEntityFromWorld(entityIdIn);

            this.entitiesById.put(entityIdIn, entityToSpawn);
            this.chunkProvider.getChunkNow(cx, cz).addEntity(entityToSpawn);

            return true;
        }
    }

    public void removeEntityFromWorld(int eid) {
        Entity entity = this.entitiesById.remove(eid);
        if (entity != null) {
            entity.remove();
            this.removeEntity(entity);
        }

    }

    private void removeEntity(Entity entityIn) {
        entityIn.detach();
        if (entityIn.addedToChunk) {
            this.getChunk(entityIn.chunkCoordX, entityIn.chunkCoordZ).removeEntity(entityIn);
        }
    }

    @Nullable
    @Override
    public Entity getEntityByID(int id) {
        return this.entitiesById.get(id);
    }

    public void notifyBlockUpdate(BlockPos pos, BlockState oldState, BlockState newState, int flags) {
        this.worldRenderer.notifyBlockUpdate(this, pos, oldState, newState, flags);
    }

    @Override
    public void markBlockRangeForRenderUpdate(BlockPos blockPosIn, BlockState oldState, BlockState newState) {
        this.worldRenderer.markBlockRangeForRenderUpdate(blockPosIn, oldState, newState);
    }

    public void markSurroundingsForRerender(int sectionX, int sectionY, int sectionZ) {
        this.worldRenderer.markSurroundingsForRerender(sectionX, sectionY, sectionZ);
    }

    //代理的东西

    @Override
    public Scoreboard getScoreboard() {
        return this.mc.world != null ? this.mc.world.getScoreboard() : null;
    }

    @Override
    public RecipeManager getRecipeManager() {
        return this.mc.world != null ? this.mc.world.getRecipeManager() : null;
    }

    @Override
    public DynamicRegistries func_241828_r() {
        return this.mc.world != null ? this.mc.world.func_241828_r() : null;
    }

    @Override
    public ITagCollectionSupplier getTags() {
        return this.mc.world != null ? this.mc.world.getTags() : null;
    }

    //无操作的项目

    @Override
    public @NotNull List<? extends PlayerEntity> getPlayers() {
        return Collections.emptyList();
    }

    @Override
    public @NotNull Biome getNoiseBiome(int x, int y, int z) {
        return BiomeRegistry.THE_VOID;
    }

    @Override
    public @NotNull Biome getNoiseBiomeRaw(int x, int y, int z) {
        return BiomeRegistry.THE_VOID;
    }

    @Override
    public @NotNull ITickList<Block> getPendingBlockTicks() {
        return EmptyTickList.get();
    }

    @Override
    public @NotNull ITickList<Fluid> getPendingFluidTicks() {
        return EmptyTickList.get();
    }

    @Nullable
    @Override
    public MapData getMapData(String mapName) {
        return null;
    }

    @Override
    public void registerMapData(MapData mapDataIn) {

    }

    @Override
    public int getNextMapId() {
        return 0;
    }

    @Override
    public float func_230487_a_(Direction p_230487_1_, boolean p_230487_2_) {
        return 0;
    }

    @Override
    public int getLightFor(LightType lightTypeIn, BlockPos blockPosIn) {
        return 0xF;
    }

    @Override
    public int getLightSubtracted(BlockPos blockPosIn, int amount) {
        return 0xF;
    }

    @Override
    public void onBlockStateChange(BlockPos pos, BlockState blockStateIn, BlockState newState) {

    }


    @Override
    public void addParticle(IParticleData particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {

    }

    @Override
    public void addParticle(IParticleData particleData, boolean forceAlwaysRender, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {

    }

    @Override
    public void addOptionalParticle(IParticleData particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {

    }

    @Override
    public void addOptionalParticle(IParticleData particleData, boolean ignoreRange, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {

    }

    @Override
    public void playEvent(int type, BlockPos pos, int data) {

    }

    @Override
    public void playEvent(@Nullable PlayerEntity player, int type, BlockPos pos, int data) {

    }

    @Override
    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {

    }

    @Override
    public void playSound(@Nullable PlayerEntity player, BlockPos pos, SoundEvent soundIn, SoundCategory category, float volume, float pitch) {

    }

    @Override
    public void playSound(@Nullable PlayerEntity player, double x, double y, double z, SoundEvent soundIn, SoundCategory category, float volume, float pitch) {

    }

    @Override
    public void playSound(double x, double y, double z, SoundEvent soundIn, SoundCategory category, float volume, float pitch, boolean distanceDelay) {

    }

    @Override
    public void playMovingSound(@Nullable PlayerEntity playerIn, Entity entityIn, SoundEvent eventIn, SoundCategory categoryIn, float volume, float pitch) {

    }

    @Override
    public void playBroadcastSound(int id, BlockPos pos, int data) {

    }
}
