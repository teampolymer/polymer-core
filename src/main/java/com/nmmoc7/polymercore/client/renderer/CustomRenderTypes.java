package com.nmmoc7.polymercore.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nmmoc7.polymercore.client.utils.AnimationTickHelper;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class CustomRenderTypes extends RenderType {
    public CustomRenderTypes(String nameIn, VertexFormat formatIn, int drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
        super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }

    public static final RenderType CUBE_NO_DEPTH = create("cube_no_depth",
        DefaultVertexFormats.POSITION_COLOR, GL11.GL_QUADS, 256,
        RenderType.State.builder()
            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
            .setDepthTestState(NO_DEPTH_TEST)
            .setAlphaState(DEFAULT_ALPHA)
            .setWriteMaskState(COLOR_DEPTH_WRITE)
            .createCompositeState(true));

    public static final RenderType CUBE_NORMAL = create("cube_normal",
        DefaultVertexFormats.POSITION_COLOR, GL11.GL_QUADS, 256,
        RenderType.State.builder()
            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
            .setAlphaState(DEFAULT_ALPHA)
            .setWriteMaskState(COLOR_DEPTH_WRITE)
            .createCompositeState(true));

    public static final RenderType FLUID = create("polymer_fluid",
        DefaultVertexFormats.NEW_ENTITY, GL11.GL_QUADS, 256, true, true,
        RenderType.State.builder()
            .setTextureState(BLOCK_SHEET_MIPPED)
            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
            .setShadeModelState(SMOOTH_SHADE)
            .setAlphaState(DEFAULT_ALPHA)
            .setLightmapState(LIGHTMAP)
            .setOverlayState(OVERLAY)
            .createCompositeState(true));

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
    public static final RenderType TRANSPARENT_BLOCK = create("projection_transparent_block",
        DefaultVertexFormats.NEW_ENTITY, GL11.GL_QUADS, 1024, true, true,
        RenderType.State.builder()
            .setTextureState(new RenderState.TextureState(PlayerContainer.BLOCK_ATLAS, false, false))
            .setTransparencyState(getConstTransparency(0.3f))
            .setDiffuseLightingState(DIFFUSE_LIGHTING)
            .setAlphaState(DEFAULT_ALPHA)
            .setLightmapState(LIGHTMAP)
            .setOverlayState(OVERLAY)
            .setWriteMaskState(COLOR_DEPTH_WRITE)
            .createCompositeState(true));

    /**
     * 半透明方块
     */
    public static final RenderType TRANSPARENT_BLOCK_DYNAMIC = create("projection_transparent_block_dynamic",
        DefaultVertexFormats.NEW_ENTITY, GL11.GL_QUADS, 1024, true, true,
        RenderType.State.builder()
            .setTextureState(new RenderState.TextureState(PlayerContainer.BLOCK_ATLAS, false, false))
            .setTransparencyState(getDynamicTransparency(0.6f, 1.0f, 30))
            .setDiffuseLightingState(DIFFUSE_LIGHTING)
            .setAlphaState(DEFAULT_ALPHA)
            .setLightmapState(LIGHTMAP)
            .setOverlayState(OVERLAY)
            .setWriteMaskState(COLOR_DEPTH_WRITE)
            .createCompositeState(true));

    public static RenderType getEntityTransparent(ResourceLocation locationIn) {
        RenderType.State state = RenderType.State.builder()
            .setTextureState(new RenderState.TextureState(locationIn, false, false))
            .setTransparencyState(getConstTransparency(0.3f))
            .setDiffuseLightingState(DIFFUSE_LIGHTING)
            .setAlphaState(DEFAULT_ALPHA)
            .setLightmapState(LIGHTMAP)
            .setOverlayState(OVERLAY)
            .createCompositeState(true);
        return create("entity_transparent", DefaultVertexFormats.NEW_ENTITY, 7, 256, true, false, state);
    }
    public static RenderType getEntityTransparentDynamic(ResourceLocation locationIn) {
        RenderType.State state = RenderType.State.builder()
            .setTextureState(new RenderState.TextureState(locationIn, false, false))
            .setTransparencyState(getDynamicTransparency(0.6f, 1.0f, 30))
            .setDiffuseLightingState(DIFFUSE_LIGHTING)
            .setAlphaState(DEFAULT_ALPHA)
            .setLightmapState(LIGHTMAP)
            .setOverlayState(OVERLAY)
            .createCompositeState(true);
        return create("entity_transparent_dynamic", DefaultVertexFormats.NEW_ENTITY, 7, 256, true, false, state);
    }


}
