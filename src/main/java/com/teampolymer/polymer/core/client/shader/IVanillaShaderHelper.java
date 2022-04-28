package com.teampolymer.polymer.core.client.shader;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.teampolymer.polymer.core.client.renderer.CustomRenderTypeBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.resources.IResourceManager;
import org.jetbrains.annotations.Nullable;

public interface IVanillaShaderHelper extends AutoCloseable {
    void init(IResourceManager manager);

    void resize(int width, int height);


    void beforeRender(float pt);

    void render(float pt);


}
