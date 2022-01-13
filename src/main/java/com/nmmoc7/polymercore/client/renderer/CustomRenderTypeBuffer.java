package com.nmmoc7.polymercore.client.renderer;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.util.Util;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.NotNull;

import java.util.SortedMap;

import net.minecraft.client.renderer.IRenderTypeBuffer.Impl;

public class CustomRenderTypeBuffer implements IRenderTypeBuffer {

    //Copied form net.minecraft.client.renderer.RenderTypeBuffers
    private final RegionRenderCacheBuilder fixedBuilder = new RegionRenderCacheBuilder();
    @SuppressWarnings("unchecked")
    private final SortedMap<RenderType, BufferBuilder> fixedBuffers = (SortedMap<RenderType, BufferBuilder>) Util.make(new Object2ObjectLinkedOpenHashMap(), (p_228485_1_) -> {
        p_228485_1_.put(Atlases.solidBlockSheet(), this.fixedBuilder.builder(RenderType.solid()));
        p_228485_1_.put(Atlases.cutoutBlockSheet(), this.fixedBuilder.builder(RenderType.cutout()));
        p_228485_1_.put(Atlases.bannerSheet(), this.fixedBuilder.builder(RenderType.cutoutMipped()));
        p_228485_1_.put(Atlases.translucentCullBlockSheet(), this.fixedBuilder.builder(RenderType.translucent()));
        put(p_228485_1_, Atlases.shieldSheet());
        put(p_228485_1_, Atlases.bedSheet());
        put(p_228485_1_, Atlases.shulkerBoxSheet());
        put(p_228485_1_, Atlases.signSheet());
        put(p_228485_1_, Atlases.chestSheet());
        put(p_228485_1_, RenderType.translucentNoCrumbling());
        put(p_228485_1_, RenderType.armorGlint());
        put(p_228485_1_, RenderType.armorEntityGlint());
        put(p_228485_1_, RenderType.glint());
        put(p_228485_1_, RenderType.glintDirect());
        put(p_228485_1_, RenderType.glintTranslucent());
        put(p_228485_1_, RenderType.entityGlint());
        put(p_228485_1_, RenderType.entityGlintDirect());
        put(p_228485_1_, RenderType.waterMask());
        ModelBakery.DESTROY_TYPES.forEach((p_228488_1_) -> {
            put(p_228485_1_, p_228488_1_);
        });
    });

    private static void put(Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder> var0, RenderType p_228486_1_) {
        var0.put(p_228486_1_, new BufferBuilder(p_228486_1_.bufferSize()));
    }

    public Impl getImpl() {
        return impl;
    }

    private final IRenderTypeBuffer.Impl impl;

    public CustomRenderTypeBuffer() {
        this.impl = IRenderTypeBuffer.immediateWithBuffers(fixedBuffers, new BufferBuilder(256));
    }

    private static final Lazy<CustomRenderTypeBuffer> lazyInstance
        = Lazy.of(CustomRenderTypeBuffer::new);

    public static CustomRenderTypeBuffer instance() {
        return lazyInstance.get();
    }

    @Override
    public @NotNull IVertexBuilder getBuffer(@NotNull RenderType renderType) {
        return impl.getBuffer(renderType);
    }


    public void finish() {
        impl.endBatch();
    }

    public void finish(RenderType renderType) {
        impl.endBatch(renderType);
    }
}

