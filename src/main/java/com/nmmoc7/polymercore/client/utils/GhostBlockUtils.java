package com.nmmoc7.polymercore.client.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.client.renderer.CustomRenderTypeBuffer;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GhostBlockUtils {

    private static IRenderTypeBuffer.Impl buffers = null;
    private static IRenderTypeBuffer.Impl buffersDynamic = null;


    public static void renderGhostBlock(BlockState blockStateIn,
                                        IRenderTypeBuffer bufferSource,
                                        MatrixStack ms,
                                        int combinedLightIn,
                                        boolean isDynamic) {
        IRenderTypeBuffer.Impl impl;
        if (bufferSource instanceof IRenderTypeBuffer.Impl) {
            impl = (IRenderTypeBuffer.Impl) bufferSource;
        } else if (bufferSource instanceof CustomRenderTypeBuffer) {
            impl = ((CustomRenderTypeBuffer) bufferSource).getImpl();
        } else {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        BlockRendererDispatcher dispatcher = mc.getBlockRenderer();
        if (isDynamic) {

            if (buffersDynamic == null) {
                buffersDynamic = initBuffers(impl, () -> AnimationTickHelper.sinCirculateIn(0.6f, 1.0f, 30));
            }
            dispatcher.renderBlock(blockStateIn, ms, buffersDynamic, combinedLightIn, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
            buffersDynamic.endBatch();
        } else {
            if (buffers == null) {
                buffers = initBuffers(impl, () -> 0.4f);
            }
            dispatcher.renderBlock(blockStateIn, ms, buffers, combinedLightIn, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
            buffers.endBatch();
        }


    }


    private static IRenderTypeBuffer.Impl initBuffers(IRenderTypeBuffer.Impl original, Supplier<Float> alphaSupplier) {
        BufferBuilder fallback = ObfuscationReflectionHelper.getPrivateValue(IRenderTypeBuffer.Impl.class, original, "builder");
        Map<RenderType, BufferBuilder> layerBuffers = ObfuscationReflectionHelper.getPrivateValue(IRenderTypeBuffer.Impl.class, original, "fixedBuffers");
        Map<RenderType, BufferBuilder> remapped = new Object2ObjectLinkedOpenHashMap<>();
        Map<RenderType, RenderType> remappedTypes = new IdentityHashMap<>();
        for (Map.Entry<RenderType, BufferBuilder> e : layerBuffers.entrySet()) {
            remapped.put(GhostRenderType.remap(remappedTypes, e.getKey(), alphaSupplier), e.getValue());
        }
        return new GhostBuffers(fallback, remapped, remappedTypes, alphaSupplier);
    }

    private static class GhostBuffers extends IRenderTypeBuffer.Impl {
        private final Map<RenderType, RenderType> remappedTypes;
        public final Supplier<Float> alphaSupplier;

        protected GhostBuffers(BufferBuilder fallback, Map<RenderType, BufferBuilder> layerBuffers, Map<RenderType, RenderType> remappedTypes, Supplier<Float> alphaSupplier) {
            super(fallback, layerBuffers);
            this.remappedTypes = remappedTypes;
            this.alphaSupplier = alphaSupplier;
        }

        @Override
        public IVertexBuilder getBuffer(RenderType type) {
            return super.getBuffer(GhostRenderType.remap(remappedTypes, type, alphaSupplier));
        }
    }

    private static class GhostRenderType extends RenderType {

        private GhostRenderType(RenderType original, Supplier<Float> alphaSupplier) {
            super(String.format("%s_%s_ghost", original.toString(), PolymerCoreApi.MOD_ID), original.format(), original.mode(), original.bufferSize(), original.affectsCrumbling(), true, () -> {
                original.setupRenderState();

                // Alter GL state
                RenderSystem.enableDepthTest();
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GlStateManager.SourceFactor.CONSTANT_ALPHA, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
                RenderSystem.blendColor(1, 1, 1, alphaSupplier.get());
            }, () -> {
                RenderSystem.blendColor(1, 1, 1, 1);
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableBlend();
                RenderSystem.disableDepthTest();

                original.clearRenderState();
            });
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other;
        }

        @Override
        public int hashCode() {
            return System.identityHashCode(this);
        }

        public static RenderType remap(Map<RenderType, RenderType> remappedTypes, RenderType in, Supplier<Float> alphaSupplier) {
            if (in instanceof GhostRenderType) {
                return in;
            } else {
                return remappedTypes.computeIfAbsent(in, it -> new GhostRenderType(it, alphaSupplier));
            }
        }
    }
}
