package com.nmmoc7.polymercore.core.mixin;

import com.nmmoc7.polymercore.core.event.BlockStateChangeEvent;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class MixinServerWorld {

    @Inject(
        method = "onBlockStateChange(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;)V",
        at = @At(value = "HEAD")
    )
    private void inject_onBlockStateChange(BlockPos pos, BlockState blockStateIn, BlockState newState, CallbackInfo ci) {
        if (blockStateIn != newState) {
            MinecraftForge.EVENT_BUS.post(new BlockStateChangeEvent(pos, blockStateIn, newState));
        }
    }
}
