package com.teampolymer.polymer.core.client.shader;

import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.teampolymer.polymer.core.PolymerCore;
import com.teampolymer.polymer.core.api.PolymerCoreApi;
import com.teampolymer.polymer.core.client.renderer.CustomRenderTypeBuffer;
import jdk.nashorn.internal.runtime.regexp.joni.WarnCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.FaceBakery;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.io.IOException;
import java.nio.IntBuffer;

public class SchematicShader implements IVanillaShaderHelper {
    private static final Logger LOGGER = LogManager.getLogger();
    private ShaderGroup shader;


    public Framebuffer getSchematicBuffer() {
        return schematicBuffer;
    }
    public Framebuffer getDynamicBuffer() {
        return dynamicBuffer;
    }

    private Framebuffer schematicBuffer;
    private Framebuffer dynamicBuffer;

    @Override
    public void init(IResourceManager manager) {

        if (shader != null) {
            close();
        }


        ResourceLocation resourcelocation = new ResourceLocation(PolymerCoreApi.MOD_ID, "shaders/post/schematic.json");

        Minecraft mc = Minecraft.getInstance();
        try {
            this.shader = new ShaderGroup(mc.getTextureManager(), manager, mc.getMainRenderTarget(), resourcelocation);
            this.schematicBuffer = this.shader.getTempTarget("schematic");
            this.dynamicBuffer = this.shader.getTempTarget("dynamic");
            this.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());

        } catch (IOException ioexception) {
            LOGGER.warn("Failed to load shader: {}", resourcelocation, ioexception);
        } catch (JsonSyntaxException jsonsyntaxexception) {
            LOGGER.warn("Failed to parse shader: {}", resourcelocation, jsonsyntaxexception);
        }
    }

    @Override
    public void resize(int width, int height) {
        shader.resize(width, height);
//        int colorTextureId = schematicBuffer.getColorTextureId();
//        GlStateManager._bindTexture(colorTextureId);
//        GlStateManager._texImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_RGBA16F, width, height, 0, GL11.GL_RGBA, GL11.GL_FLOAT, null);
//        GlStateManager._bindTexture(0);
    }


    @Override
    public void beforeRender(float pt) {
        if (shader == null) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        schematicBuffer.clear(Minecraft.ON_OSX);
        schematicBuffer.copyDepthFrom(mc.getMainRenderTarget());
        dynamicBuffer.clear(Minecraft.ON_OSX);
        dynamicBuffer.copyDepthFrom(mc.getMainRenderTarget());
    }

    @Override
    public void render(float pt) {
        if (shader == null) {
            return;
        }
        shader.process(pt);
    }

    @Override
    public void close() {
        if (shader == null) {
            return;
        }

        shader.close();
        shader = null;


    }
}
