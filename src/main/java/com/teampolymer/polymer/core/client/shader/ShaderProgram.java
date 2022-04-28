package com.teampolymer.polymer.core.client.shader;

import net.minecraft.client.shader.IShaderManager;
import net.minecraft.client.shader.ShaderInstance;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.client.shader.ShaderLoader;
import net.minecraft.resources.IResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ShaderProgram implements IShaderManager {
    private static final Logger LOGGER = LogManager.getLogger();
    private final int program;
    private final ShaderLoader vertexShader;
    private final ShaderLoader fragmentShader;

    public ShaderProgram(int program, ShaderLoader vertexShader, ShaderLoader fragmentShader) {
        this.program = program;
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
    }

    public static ShaderProgram create(IResourceManager manager, String fragShader, String vertShader) {
        try {
            ShaderLoader vert = ShaderInstance.getOrCreate(manager, ShaderLoader.ShaderType.VERTEX, vertShader);
            ShaderLoader frag = ShaderInstance.getOrCreate(manager, ShaderLoader.ShaderType.FRAGMENT, fragShader);
            int programId = ShaderLinkHelper.createProgram();
            ShaderProgram program = new ShaderProgram(programId, vert, frag);
            ShaderLinkHelper.linkProgram(program);
            return program;
        } catch (IOException ex) {
            LOGGER.error("Failed to load program with vert {}, frag {}", vertShader, fragShader, ex);
        }
        return null;
    }

    @Override
    public int getId() {
        return program;
    }

    @Override
    public void markDirty() {

    }

    @Override
    public ShaderLoader getVertexProgram() {
        return vertexShader;
    }

    @Override
    public ShaderLoader getFragmentProgram() {
        return fragmentShader;
    }

    public void close() {
        ShaderLinkHelper.releaseProgram(this);
    }

    public void use() {
        ShaderLinkHelper.glUseProgram(program);
    }

    public void release() {
        ShaderLinkHelper.glUseProgram(0);
    }
}
