package com.nmmoc7.polymercore.client.renderer;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.util.Util;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.NotNull;

import java.util.SortedMap;

public class CustomRenderTypeBuffer implements IRenderTypeBuffer {

    //Copied form net.minecraft.client.renderer.RenderTypeBuffers
    private final RegionRenderCacheBuilder fixedBuilder = new RegionRenderCacheBuilder();
    @SuppressWarnings("unchecked")
    private final SortedMap<RenderType, BufferBuilder> fixedBuffers = (SortedMap<RenderType, BufferBuilder>) Util.make(new Object2ObjectLinkedOpenHashMap(), (p_228485_1_) -> {
        p_228485_1_.put(Atlases.getSolidBlockType(), this.fixedBuilder.getBuilder(RenderType.getSolid()));
        p_228485_1_.put(Atlases.getCutoutBlockType(), this.fixedBuilder.getBuilder(RenderType.getCutout()));
        p_228485_1_.put(Atlases.getBannerType(), this.fixedBuilder.getBuilder(RenderType.getCutoutMipped()));
        p_228485_1_.put(Atlases.getTranslucentCullBlockType(), this.fixedBuilder.getBuilder(RenderType.getTranslucent()));
        put(p_228485_1_, Atlases.getShieldType());
        put(p_228485_1_, Atlases.getBedType());
        put(p_228485_1_, Atlases.getShulkerBoxType());
        put(p_228485_1_, Atlases.getSignType());
        put(p_228485_1_, Atlases.getChestType());
        put(p_228485_1_, RenderType.getTranslucentNoCrumbling());
        put(p_228485_1_, RenderType.getArmorGlint());
        put(p_228485_1_, RenderType.getArmorEntityGlint());
        put(p_228485_1_, RenderType.getGlint());
        put(p_228485_1_, RenderType.getGlintDirect());
        put(p_228485_1_, RenderType.getGlintTranslucent());
        put(p_228485_1_, RenderType.getEntityGlint());
        put(p_228485_1_, RenderType.getEntityGlintDirect());
        put(p_228485_1_, RenderType.getWaterMask());
        ModelBakery.DESTROY_RENDER_TYPES.forEach((p_228488_1_) -> {
            put(p_228485_1_, p_228488_1_);
        });
    });

    private static void put(Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder> var0, RenderType p_228486_1_) {
        var0.put(p_228486_1_, new BufferBuilder(p_228486_1_.getBufferSize()));
    }

    public Impl getImpl() {
        return impl;
    }

    private final IRenderTypeBuffer.Impl impl;

    public CustomRenderTypeBuffer() {
        this.impl = IRenderTypeBuffer.getImpl(fixedBuffers, new BufferBuilder(256));
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
        impl.finish();
    }

    public void finish(RenderType renderType) {
        impl.finish(renderType);
    }
}

