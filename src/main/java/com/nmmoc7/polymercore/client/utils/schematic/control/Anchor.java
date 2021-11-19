package com.nmmoc7.polymercore.client.utils.schematic.control;

import com.nmmoc7.polymercore.api.capability.IMultiblockLocateHandler;
import com.nmmoc7.polymercore.client.handler.MultiblockSchematicHandler;
import com.nmmoc7.polymercore.client.resources.Icons;
import com.nmmoc7.polymercore.common.registry.KeysRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.Objects;

public class Anchor extends ControlAction {

    public Anchor() {
        super(Icons.LOCATE);
    }

    @Override
    public ITextComponent getName() {
        return new TranslationTextComponent("gui.polymer.locator.control.anchor.title");
    }

    @Override
    public List<ITextComponent> getDescription() {
        return super.getDescription();
    }

    @Override
    public void handleMouseInput(IMultiblockLocateHandler locateHandler, int mouseButton, boolean pressed) {
        if (!pressed || mouseButton != 1) {
            return;
        }

        if (Minecraft.getInstance().gameSettings.keyBindSneak.isKeyDown()) {
            return;
        }

        BlockPos oldOffset = locateHandler.getOffset();
        BlockPos newOffset = MultiblockSchematicHandler.INSTANCE.findTracePos();
        if (locateHandler.isAnchored() && Objects.equals(oldOffset, newOffset)) {
            return;
        }
        locateHandler.setOffset(newOffset);
        locateHandler.setAnchored(newOffset != null);
        callback.run();
    }
}
