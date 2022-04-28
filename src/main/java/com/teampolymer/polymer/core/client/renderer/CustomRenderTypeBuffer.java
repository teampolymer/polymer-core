package com.teampolymer.polymer.core.client.renderer;

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
    private final SortedMap<RenderType, BufferBuilder> fixedBuffers = (SortedMap<RenderType, BufferBuilder>) Util.make(new Object2ObjectLinkedOpenHashMap(), (map) -> {
        map.put(Atlases.solidBlockSheet(), this.fixedBuilder.builder(RenderType.solid()));
        map.put(Atlases.cutoutBlockSheet(), this.fixedBuilder.builder(RenderType.cutout()));
        map.put(Atlases.bannerSheet(), this.fixedBuilder.builder(RenderType.cutoutMipped()));
        map.put(Atlases.translucentCullBlockSheet(), this.fixedBuilder.builder(RenderType.translucent()));
        put(map, Atlases.shieldSheet());
        put(map, Atlases.bedSheet());
        put(map, Atlases.shulkerBoxSheet());
        put(map, Atlases.signSheet());
        put(map, Atlases.chestSheet());
        put(map, RenderType.translucentNoCrumbling());
        put(map, RenderType.armorGlint());
        put(map, RenderType.armorEntityGlint());
        put(map, RenderType.glint());
        put(map, RenderType.glintDirect());
        put(map, RenderType.glintTranslucent());
        put(map, RenderType.entityGlint());
        put(map, RenderType.entityGlintDirect());
        put(map, RenderType.waterMask());
        ModelBakery.DESTROY_TYPES.forEach((p_228488_1_) -> {
            put(map, p_228488_1_);
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
        impl.endBatch(CustomRenderTypes.SCHEMATIC_TRANSPARENT_NO_DEPTH);
        impl.endBatch(CustomRenderTypes.SCHEMATIC_TRANSPARENT);
    }

    public void finish(RenderType renderType) {
        impl.endBatch(renderType);
    }
}

