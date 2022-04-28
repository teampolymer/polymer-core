package com.teampolymer.polymer.core.client.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teampolymer.polymer.core.client.renderer.ColoredVertexBuilder;
import com.teampolymer.polymer.core.client.renderer.CustomRenderTypes;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.WeakHashMap;

public class GhostBlockUtils {

    public enum GhostRenderType {
        NONE,
        STATIC,
        ANIMATED,
        ERROR
    }

    public static void renderGhostBlock(BlockState blockStateIn,
                                        IRenderTypeBuffer bufferSource,
                                        MatrixStack ms,
                                        int combinedLightIn,
                                        GhostRenderType type) {

        Minecraft mc = Minecraft.getInstance();
        BlockRendererDispatcher dispatcher = mc.getBlockRenderer();

        dispatcher.renderBlock(blockStateIn, ms, wrap(bufferSource, type), combinedLightIn, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);


    }

    public static IRenderTypeBuffer wrap(IRenderTypeBuffer bufferSource, GhostRenderType type) {
        switch (type) {
            case STATIC:
                return GhostBlockUtils.wrap(bufferSource, false);
            case ANIMATED:
                return GhostBlockUtils.wrap(bufferSource, true);
            case ERROR:
                return GhostBlockUtils.wrapError(bufferSource);
            default:
                return bufferSource;
        }
    }


    public static IVertexBuilder wrap(IVertexBuilder buffer, GhostRenderType type) {
        switch (type) {
            case STATIC:
                return GhostBlockUtils.wrap(buffer, false);
            case ANIMATED:
                return GhostBlockUtils.wrap(buffer, true);
            case ERROR:
                return GhostBlockUtils.wrapError(buffer);
            default:
                return buffer;
        }
    }

    public static IVertexBuilder wrap(IVertexBuilder delegate, boolean isDynamic) {
        float factor = 0.5f;
        if (isDynamic) {
            factor = AnimationTickHelper.sinCirculateIn(0.6f, 1.0f, 30);
        }
        return new ColoredVertexBuilder(delegate, 1.0f, 1.0f, 1.0f, factor);
    }

    public static IVertexBuilder wrapError(IVertexBuilder delegate) {
        return new ColoredVertexBuilder(delegate, 1.0f, 0.2f, 0.16f, 0.8f);
    }


    private static final WeakHashMap<IRenderTypeBuffer, IRenderTypeBuffer> staticGhostBuffers = new WeakHashMap<>();
    private static final WeakHashMap<IRenderTypeBuffer, IRenderTypeBuffer> dynamicGhostBuffers = new WeakHashMap<>();
    private static final WeakHashMap<IRenderTypeBuffer, IRenderTypeBuffer> errorGhostBuffers = new WeakHashMap<>();


    public static IRenderTypeBuffer wrapError(IRenderTypeBuffer delegate) {
        return errorGhostBuffers.computeIfAbsent(delegate, GhostBlockUtils::wrapErrorImpl);
    }

    public static IRenderTypeBuffer wrap(IRenderTypeBuffer delegate, boolean isDynamic) {
        if (isDynamic) {
            return dynamicGhostBuffers.computeIfAbsent(delegate, GhostBlockUtils::wrapDynamicImpl);
        }
        return staticGhostBuffers.computeIfAbsent(delegate, GhostBlockUtils::wrapStaticImpl);
    }


    private static IRenderTypeBuffer wrapStaticImpl(IRenderTypeBuffer delegate) {
        return (type) -> wrap(delegate.getBuffer(CustomRenderTypes.schematicFor(type, true)), false);
    }

    private static IRenderTypeBuffer wrapDynamicImpl(IRenderTypeBuffer delegate) {
        return (type) -> wrap(delegate.getBuffer(CustomRenderTypes.schematicFor(type, true)), true);
    }

    public static IRenderTypeBuffer wrapErrorImpl(IRenderTypeBuffer delegate) {
        return (type) -> wrapError(delegate.getBuffer(CustomRenderTypes.schematicFor(type, false)));

    }


}
