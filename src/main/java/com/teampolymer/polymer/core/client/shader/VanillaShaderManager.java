package com.teampolymer.polymer.core.client.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class VanillaShaderManager {
    private static List<IVanillaShaderHelper> shaders = new ArrayList<>();

    public static <T extends IVanillaShaderHelper> T addShader(T shader) {
        shaders.add(shader);
        return shader;
    }

    public static List<IVanillaShaderHelper> getShaders() {
        return shaders;
    }

    public static void forEachShader(Consumer<IVanillaShaderHelper> consumer) {
        for (IVanillaShaderHelper shader : shaders) {
            consumer.accept(shader);
        }
    }


//    public static final SchematicShader schematicShader = addShader(new SchematicShader());


    public static void init(IResourceManager manager) {

        RenderSystem.recordRenderCall(() -> {
            for (IVanillaShaderHelper shader : shaders) {
                shader.init(manager);
            }
        });
    }
}
