package com.nmmoc7.polymercore.client.utils.schematic.control;

import com.nmmoc7.polymercore.api.capability.IMultiblockLocateHandler;
import com.nmmoc7.polymercore.client.resources.IGuiResource;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public abstract class ControlAction {
    private final IGuiResource icon;
    private boolean enabled;
    protected Runnable callback = () -> {
    };

    protected ControlAction(IGuiResource icon) {
        this.icon = icon;
        this.enabled = true;
    }


    public List<ITextComponent> getDescription() {
        return Collections.emptyList();
    }

    public abstract ITextComponent getName();

    public IGuiResource getIcon() {
        return icon;
    }

    public void setCallback(@NotNull Runnable callback) {
        this.callback = callback;
    }

    public void update(IMultiblockLocateHandler locateHandler) {

    }

    public boolean shouldHideSchematic() {
        return false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void handleMouseInput(IMultiblockLocateHandler locateHandler, int mouseButton, boolean pressed) {

    }

    public void handleKeyInput(IMultiblockLocateHandler locateHandler, int key, boolean pressed) {
    }

    public boolean handleMouseScrolled(IMultiblockLocateHandler locateHandler, double delta) {
        return false;
    }
}
