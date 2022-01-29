package com.teampolymer.polymer.core.client.utils.schematic.control;

import com.google.common.collect.Lists;
import com.teampolymer.polymer.core.api.capability.IMultiblockLocateHandler;
import com.teampolymer.polymer.core.client.resources.Icons;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collections;
import java.util.List;

public class Flip extends ControlAction {

    public Flip() {
        super(Icons.FLIP);
    }

    @Override
    public ITextComponent getName() {
        return new TranslationTextComponent("gui.polymer.locator.control.flip.title");
    }

    private final List<ITextComponent> description = Lists.newArrayList(
        new TranslationTextComponent("gui.polymer.locator.control.flip.description_1"),
        new TranslationTextComponent("gui.polymer.locator.control.flip.description_2"),
        new TranslationTextComponent("gui.polymer.locator.control.flip.description_3")
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
        if (Screen.hasAltDown()) {
            locateHandler.flip();
            callback.run();
        }
    }
}