package com.teampolymer.polymer.core.client.utils.schematic.control;

import com.google.common.collect.Lists;
import com.teampolymer.polymer.core.api.capability.IMultiblockLocateHandler;
import com.teampolymer.polymer.core.client.handler.MultiblockSchematicHandler;
import com.teampolymer.polymer.core.client.resources.Icons;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collections;
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

    private final List<ITextComponent> description = Lists.newArrayList(
        new TranslationTextComponent("gui.polymer.locator.control.anchor.description_1"),
        new TranslationTextComponent("gui.polymer.locator.control.anchor.description_2"),
        new TranslationTextComponent("gui.polymer.locator.control.anchor.description_3")
    );

    @Override
    public List<ITextComponent> getDescription() {
        return Collections.unmodifiableList(description);
    }

    @Override
    public void handleMouseInput(IMultiblockLocateHandler locateHandler, int mouseButton, boolean pressed) {
        if (!pressed || mouseButton != 1) {
            return;
        }

        if (Minecraft.getInstance().options.keyShift.isDown()) {
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
