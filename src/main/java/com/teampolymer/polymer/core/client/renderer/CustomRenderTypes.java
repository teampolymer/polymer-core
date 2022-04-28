package com.teampolymer.polymer.core.client.renderer;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
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

    /**
     * 半透明方块
     */
    public static final RenderType SCHEMATIC_TRANSPARENT = create("schematic_transparent",
        DefaultVertexFormats.NEW_ENTITY, GL11.GL_QUADS, 1024, true, true,
        RenderType.State.builder()
            .setTextureState(BLOCK_SHEET_MIPPED)
            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
            .setDiffuseLightingState(DIFFUSE_LIGHTING)
            .setAlphaState(DEFAULT_ALPHA)
            .setLightmapState(LIGHTMAP)
            .setOverlayState(OVERLAY)
            .createCompositeState(true));
    public static final RenderType SCHEMATIC_TRANSPARENT_NO_DEPTH = create("schematic_transparent_block_no_depth",
        DefaultVertexFormats.NEW_ENTITY, GL11.GL_QUADS, 1024, true, true,
        RenderType.State.builder()
            .setTextureState(BLOCK_SHEET_MIPPED)
            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
            .setDiffuseLightingState(DIFFUSE_LIGHTING)
            .setAlphaState(DEFAULT_ALPHA)
            .setLightmapState(LIGHTMAP)
            .setDepthTestState(NO_DEPTH_TEST)
            .setOverlayState(OVERLAY)
            .createCompositeState(true));


    public static RenderType schematicFor(RenderType original, boolean enableDepthTest) {
        if (!(original instanceof Type)) {
            return original;
        }

        String name = original.name + "_schematic";

        Type type = (Type) original;
        State oldState = type.state;

        ImmutableList<RenderState> entries = oldState.states;

        DepthTestState depthTestState = (DepthTestState) entries.get(5);

        boolean modified = false;
        if (enableDepthTest && depthTestState != LEQUAL_DEPTH_TEST) {
            depthTestState = LEQUAL_DEPTH_TEST;
            modified = true;
        } else if (!enableDepthTest && depthTestState != NO_DEPTH_TEST) {
            depthTestState = NO_DEPTH_TEST;
            modified = true;
        }

        if (entries.get(1) == TRANSLUCENT_TRANSPARENCY && !modified) {
            return original;
        }

        State state = State.builder()
            .setTextureState((TextureState) entries.get(0))
            .setTransparencyState(RenderState.TRANSLUCENT_TRANSPARENCY)
            .setDiffuseLightingState((DiffuseLightingState) entries.get(2))
            .setShadeModelState((ShadeModelState) entries.get(3))
            .setAlphaState((AlphaState) entries.get(4))
            .setDepthTestState(depthTestState)
            .setCullState((CullState) entries.get(6))
            .setLightmapState((LightmapState) entries.get(7))
            .setOverlayState((OverlayState) entries.get(8))
            .setFogState((FogState) entries.get(9))
            .setLayeringState((LayerState) entries.get(10))
            .setOutputState((TargetState) entries.get(11))
            .setTexturingState((TexturingState) entries.get(12))
            .setWriteMaskState((WriteMaskState) entries.get(13))
            .setLineState((LineState) entries.get(14))
            .createCompositeState(oldState.outlineProperty);

        return create(name,
            original.format(),
            original.mode(),
            original.bufferSize(),
            original.affectsCrumbling(),
            true,
            state);
    }

}
