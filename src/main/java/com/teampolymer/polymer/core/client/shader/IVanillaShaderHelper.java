package com.teampolymer.polymer.core.client.shader;

import net.minecraft.resources.IResourceManager;

public interface IVanillaShaderHelper extends AutoCloseable {
    void init(IResourceManager manager);

    void resize(int width, int height);


    void beforeRender(float pt);

    void postRender(float pt);


}
