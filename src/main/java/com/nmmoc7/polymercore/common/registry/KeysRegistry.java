package com.nmmoc7.polymercore.common.registry;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;

public class KeysRegistry {
    private static final Set<KeyBinding> allKeys = new HashSet<>();

    public static void init() {
        for (KeyBinding allKey : allKeys) {
            ClientRegistry.registerKeyBinding(allKey);
        }
    }

    public static KeyBinding newKey(String description, IKeyConflictContext context, KeyModifier modifier, final InputMappings.Type inputType, final int keyCode) {
        KeyBinding keyBinding = new KeyBinding(description, context, modifier, inputType, keyCode, "key.category." + PolymerCoreApi.MOD_ID);
        allKeys.add(keyBinding);
        return keyBinding;
    }

    public static KeyBinding newKey(String description, IKeyConflictContext context, final InputMappings.Type inputType, final int keyCode) {
        return newKey(description, context, KeyModifier.NONE, inputType, keyCode);
    }

    public static final KeyBinding TOOL_CTRL_KEY = newKey(
        "key.message", KeyConflictContext.IN_GAME,
        InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_G
    );

}
