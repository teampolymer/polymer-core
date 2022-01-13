package com.nmmoc7.polymercore.client.event;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.client.handler.MultiblockSchematicHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = PolymerCoreApi.MOD_ID)
public class InputEventHandler {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        int key = event.getKey();
        boolean pressed = !(event.getAction() == 0);

        if (Minecraft.getInstance().screen != null)
            return;


        MultiblockSchematicHandler.INSTANCE.onKeyInput(key, pressed);

    }

    @SubscribeEvent
    public static void onMouseScrolled(InputEvent.MouseScrollEvent event) {
        if (Minecraft.getInstance().screen != null)
            return;

        double delta = event.getScrollDelta();
        boolean cancel = false;
        cancel |= MultiblockSchematicHandler.INSTANCE.onMouseScrolled(delta);

        event.setCanceled(cancel);


    }

    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseInputEvent event) {
        if (Minecraft.getInstance().screen != null)
            return;

        int button = event.getButton();
        boolean pressed = !(event.getAction() == 0);

        MultiblockSchematicHandler.INSTANCE.onMouseInput(button, pressed);
    }
}
