package com.teampolymer.polymer.core.client.shader;

import com.teampolymer.polymer.core.api.PolymerCoreApi;
import net.minecraft.resources.IResourceManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NormalShaderManager {
    private static List<ShaderProgram> shaders = new ArrayList<>();



    public static void releaseAll() {
        Iterator<ShaderProgram> it = shaders.iterator();
        while (it.hasNext()) {
            ShaderProgram next = it.next();
            next.close();
            it.remove();
        }
    }
    public static void init(IResourceManager manager) {
        releaseAll();

    }
}
