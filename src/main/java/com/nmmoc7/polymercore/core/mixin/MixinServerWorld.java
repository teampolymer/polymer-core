package com.nmmoc7.polymercore.core.mixin;

import com.nmmoc7.polymercore.core.event.BlockUpdateEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.profiler.IProfiler;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.ISpawnWorldInfo;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class MixinServerWorld extends World {

    private MixinServerWorld(ISpawnWorldInfo worldInfo, RegistryKey<World> dimension, DimensionType dimensionType, Supplier<IProfiler> profiler, boolean isRemote, boolean isDebug, long seed) {
        super(worldInfo, dimension, dimensionType, profiler, isRemote, isDebug, seed);
    }

    @Inject(
        method = "updateBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V",
        at = @At(value = "HEAD")
    )
    private void inject_updateBlock(BlockPos pos, Block block, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new BlockUpdateEvent(this, pos, block));
    }
}
