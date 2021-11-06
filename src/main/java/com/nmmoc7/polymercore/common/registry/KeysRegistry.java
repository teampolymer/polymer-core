package com.nmmoc7.polymercore.common.registry;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

public class KeysRegistry {
    public static final KeyBinding TOOL_CTRL_KEY = new KeyBinding("key.message",
        KeyConflictContext.IN_GAME,
        KeyModifier.NONE,
        InputMappings.Type.KEYSYM,
        GLFW.GLFW_KEY_G,
        "key.category." + PolymerCoreApi.MOD_ID);
}
