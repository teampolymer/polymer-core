package com.nmmoc7.polymercore.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.inventory.container.PlayerContainer;
import org.lwjgl.opengl.GL11;

import java.util.OptionalDouble;

public class ProjectionRenderType extends RenderType {
    public ProjectionRenderType(String nameIn, VertexFormat formatIn, int drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
        super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }

    private static final LineState THICK_LINES = new LineState(OptionalDouble.of(3.0D));

    public static final RenderType OVERLAY_LINES = makeType("overlay_lines",
        DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 256,
        RenderType.State.getBuilder()
            .line(THICK_LINES)
            .layer(VIEW_OFFSET_Z_LAYERING)
            .transparency(TRANSLUCENT_TRANSPARENCY)
            .texture(NO_TEXTURE)
            .depthTest(RenderState.DEPTH_LEQUAL)
            .cull(CULL_DISABLED)
            .lightmap(LIGHTMAP_DISABLED)
            .writeMask(COLOR_WRITE)
            .build(false));

    /**
     * 半透明混合
     */
    protected static final RenderState.TransparencyState CONST_TRANSPARENCY = new RenderState.TransparencyState("translucent_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.CONSTANT_ALPHA, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
        //发现这段额可以放外面，好耶！
//        RenderSystem.blendColor(1, 1, 1, 0.4f);
    }, () -> {
//        RenderSystem.blendColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    public static final RenderType TRANSPARENT_BLOCK = makeType("projection_transparent_block",
        DefaultVertexFormats.ENTITY, GL11.GL_QUADS, 256, true, true,
        RenderType.State.getBuilder()
            .texture(new RenderState.TextureState(PlayerContainer.LOCATION_BLOCKS_TEXTURE, false, false))
            .transparency(CONST_TRANSPARENCY)
            .diffuseLighting(DIFFUSE_LIGHTING_ENABLED)
            .alpha(DEFAULT_ALPHA)
            .lightmap(LIGHTMAP_ENABLED)
            .overlay(OVERLAY_ENABLED)
            .depthTest(DEPTH_ALWAYS)
            .build(true));


}
