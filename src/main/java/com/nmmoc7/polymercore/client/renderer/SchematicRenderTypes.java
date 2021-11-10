package com.nmmoc7.polymercore.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nmmoc7.polymercore.client.utils.AnimationTickHelper;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.inventory.container.PlayerContainer;
import org.lwjgl.opengl.GL11;

public class SchematicRenderTypes extends RenderType {
    public SchematicRenderTypes(String nameIn, VertexFormat formatIn, int drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
        super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }

    public static final RenderType CUBE_NO_DEPTH = makeType("cube_no_depth",
        DefaultVertexFormats.POSITION_COLOR, GL11.GL_QUADS, 256,
        RenderType.State.getBuilder()
            .transparency(TRANSLUCENT_TRANSPARENCY)
            .depthTest(DEPTH_ALWAYS)
            .alpha(DEFAULT_ALPHA)
            .writeMask(COLOR_DEPTH_WRITE)
            .build(true));

    /**
     * 半透明混合
     */
    protected static RenderState.TransparencyState getConstTransparency(float alpha) {
        return new RenderState.TransparencyState("translucent_transparency_const", () -> {
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.CONSTANT_ALPHA, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
            RenderSystem.blendColor(1, 1, 1, alpha);
        }, () -> {
            RenderSystem.blendColor(1, 1, 1, 1);
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        });
    }

    /**
     * 半透明混合
     */
    protected static RenderState.TransparencyState getDynamicTransparency(float begin, float end, float period) {
        return new RenderState.TransparencyState("translucent_transparency_dynamic", () -> {
            float alpha = AnimationTickHelper.sinCirculateIn(begin, end, period);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.CONSTANT_ALPHA, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
            RenderSystem.blendColor(1, 1, 1, alpha);
        }, () -> {
            RenderSystem.blendColor(1, 1, 1, 1);
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        });
    }


    /**
     * 半透明方块
     */
    public static final RenderType TRANSPARENT_BLOCK = makeType("projection_transparent_block",
        DefaultVertexFormats.ENTITY, GL11.GL_QUADS, 1024, true, true,
        RenderType.State.getBuilder()
            .texture(new RenderState.TextureState(PlayerContainer.LOCATION_BLOCKS_TEXTURE, false, false))
            .transparency(getConstTransparency(0.3f))
            .diffuseLighting(DIFFUSE_LIGHTING_ENABLED)
            .alpha(DEFAULT_ALPHA)
            .lightmap(LIGHTMAP_ENABLED)
            .overlay(OVERLAY_ENABLED)
            .writeMask(COLOR_DEPTH_WRITE)
            .build(true));

    /**
     * 半透明方块
     */
    public static final RenderType TRANSPARENT_BLOCK_DYNAMIC = makeType("projection_transparent_block_dynamic",
        DefaultVertexFormats.ENTITY, GL11.GL_QUADS, 1024, true, true,
        RenderType.State.getBuilder()
            .texture(new RenderState.TextureState(PlayerContainer.LOCATION_BLOCKS_TEXTURE, false, false))
            .transparency(getDynamicTransparency(0.6f, 1.0f, 30))
            .diffuseLighting(DIFFUSE_LIGHTING_ENABLED)
            .alpha(DEFAULT_ALPHA)
            .lightmap(LIGHTMAP_ENABLED)
            .overlay(OVERLAY_ENABLED)
            .writeMask(COLOR_DEPTH_WRITE)
            .build(true));


}
