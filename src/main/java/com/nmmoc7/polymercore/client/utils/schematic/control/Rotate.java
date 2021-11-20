package com.nmmoc7.polymercore.client.utils.schematic.control;

import com.google.common.collect.Lists;
import com.nmmoc7.polymercore.api.capability.IMultiblockLocateHandler;
import com.nmmoc7.polymercore.client.resources.Icons;
import com.nmmoc7.polymercore.common.registry.KeysRegistry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collections;
import java.util.List;

public class Rotate extends ControlAction {
    private static final String TITLE = "gui.polymer.locator.control.rotate.title";
    private static final Rotation[] ROTATIONS = Rotation.values();

    public Rotate() {
        super(Icons.ROTATE);
    }

    @Override
    public ITextComponent getName() {
        return new TranslationTextComponent(TITLE);
    }

    private final List<ITextComponent> description = Lists.newArrayList(
        new TranslationTextComponent("gui.polymer.locator.control.rotate.description_1"),
        new TranslationTextComponent("gui.polymer.locator.control.rotate.description_2")
    );

    @Override
    public List<ITextComponent> getDescription() {
        return Collections.unmodifiableList(description);
    }

    @Override
    public boolean handleMouseScrolled(IMultiblockLocateHandler locateHandler, double delta) {
        if (Screen.hasAltDown()) {
            int dir = MathHelper.clamp((int) delta, -1, 1);
            int current = locateHandler.getRotation().ordinal();
            int index = ((dir + current) % 4 + 4) % 4;
            Rotation rotation = ROTATIONS[index];
            locateHandler.setRotation(rotation);
            callback.run();
            return true;
        }
        return false;
    }
}
