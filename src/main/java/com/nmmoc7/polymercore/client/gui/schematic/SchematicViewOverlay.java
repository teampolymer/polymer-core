package com.nmmoc7.polymercore.client.gui.schematic;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nmmoc7.polymercore.api.capability.IMultiblockLocateHandler;
import com.nmmoc7.polymercore.client.resources.GuiResources;
import com.nmmoc7.polymercore.client.resources.IGuiResource;
import com.nmmoc7.polymercore.client.utils.Color;
import com.nmmoc7.polymercore.client.utils.schematic.control.*;
import com.nmmoc7.polymercore.common.registry.KeysRegistry;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.Locale;

/**
 * 蓝图控制的窗口
 * Modified from create
 * com.simibubi.create.foundation.gui.ToolSelectionScreen
 * Copyright (c) 2019 simibubi
 */
public class SchematicViewOverlay extends Screen {

    public final String scrollToCycle = I18n.get("gui.polymer.toolmenu.cycle");
    public final String holdToFocus = "gui.polymer.toolmenu.focusKey";

    private final ControlAction[] actions;
    private int current = 0;
    private int w;
    private int h;
    private float yOffset = 0;
    private boolean focused = false;

    //不创建本类的实例，因为它并不是一个gui而是一个Overlay，继承screen只是为了方便调用一些渲染函数
    public SchematicViewOverlay(Runnable callback) {
        super(StringTextComponent.EMPTY);
        actions = new ControlAction[]{
            new Anchor(),
            new Rotate(),
            new Flip(),
            new MoveX(),
            new MoveY(),
            new MoveZ(),
            new Assemble(),
        };

        for (ControlAction action : actions) {
            action.setCallback(callback);
        }


        w = Math.max(actions.length * 40 + 30, 220);
        h = 30;
    }

    private boolean initialized = false;

    public void renderOverlay(MatrixStack ms, float pt) {
        Minecraft mc = Minecraft.getInstance();
        MainWindow mainWindow = mc.getWindow();
        if (!initialized)
            init(mc, mainWindow.getGuiScaledWidth(), mainWindow.getGuiScaledHeight());

        int x = (mainWindow.getGuiScaledWidth() - w) / 2 + 15;
        int y = mainWindow.getGuiScaledHeight() - h - 75;

        ms.pushPose();
        ms.translate(0, -yOffset, focused ? 100 : 0);

        IGuiResource gray = GuiResources.TOOLBAR_BACKGROUND;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(1, 1, 1, focused ? 3 / 4f : .3f);

        Minecraft.getInstance()
            .getTextureManager()
            .bind(gray.getLocation());
        blit(ms, x - 15, y, gray.getStartX(), gray.getStartY(), w, h, gray.getWidth(), gray.getHeight());

        float toolTipAlpha = yOffset / 10;
        List<ITextComponent> toolTip = currentAction().getDescription();
        int stringAlphaComponent = ((int) (toolTipAlpha * 0xFF)) << 24;

        if (toolTipAlpha > 0.25f) {
            int height = h + 22 + 10 * (toolTip.size() - 1);
            boolean disabled = !currentAction().isEnabled();
            if (disabled) {
                height += 12;
            }
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.color4f(.7f, .7f, .8f, toolTipAlpha);
            blit(ms, x - 15, y, gray.getStartX(), gray.getStartY(), w, height, gray.getWidth(), gray.getHeight());
            RenderSystem.color4f(1, 1, 1, 1);
            RenderSystem.disableBlend();

            int yOff = y;
            if (toolTip.size() > 0) {
                yOff += 38;
                font.draw(ms, toolTip.get(0), x - 10, yOff, 0xEEEEEE + stringAlphaComponent);
            }
            if (toolTip.size() > 1) {
                yOff += 12;
                font.draw(ms, toolTip.get(1), x - 10, yOff, 0xCCDDFF + stringAlphaComponent);
            }
            if (toolTip.size() > 2) {
                yOff += 10;
                font.draw(ms, toolTip.get(2), x - 10, yOff, 0xCCDDFF + stringAlphaComponent);
            }
            if (toolTip.size() > 3) {
                yOff += 10;
                font.draw(ms, toolTip.get(3), x - 10, yOff, 0xCCDDFF + stringAlphaComponent);
            }
            if (disabled) {
                yOff += 12;
                font.draw(ms, new TranslationTextComponent("gui.polymer.locator.control.misc.disabled"), x - 10, yOff, 0xCCCCDD + stringAlphaComponent);
            }
        }

        RenderSystem.color4f(1, 1, 1, 1);
        if (actions.length > 1) {

            String keyName = KeysRegistry.TOOL_CTRL_KEY.getTranslatedKeyMessage().getString().toUpperCase(Locale.ROOT);
            int width = minecraft.getWindow()
                .getGuiScaledWidth();
            if (!focused)
                drawCenteredString(ms, font, I18n.get(holdToFocus, keyName), width / 2,
                    y - 10, 0xCCDDFF);
            else
                drawCenteredString(ms, font, scrollToCycle, width / 2, y - 10, 0xCCDDFF);
        } else {
            x += 65;
        }


        for (int i = 0; i < actions.length; i++) {
            ms.pushPose();

            float alpha = focused ? 1 : .2f;
            if (i == current) {
                ms.translate(0, -10, 0);
                drawCenteredString(ms, font, actions[i]
                    .getName(), x + i * 40 + 24, y + 28, 0xCCDDFF);
                alpha = 1;
            }
            IGuiResource icon = actions[i]
                .getIcon();

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.color4f(0, 0, 0, alpha * .2f);
            icon.draw(ms, 0, x + i * 40 + 17, y + 12);
            if (actions[i].isEnabled()) {
                RenderSystem.color4f(1, 1, 1, alpha);
            } else {
                RenderSystem.color4f(.7f, .7f, .7f, alpha);
            }
            icon.draw(ms, 0, x + i * 40 + 16, y + 11);
            ms.popPose();
        }

        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        ms.popPose();
    }

    public void update(IMultiblockLocateHandler locateHandler) {
        if (focused && locateHandler != null)
            yOffset += (10 - yOffset) * .1f;
        else
            yOffset *= .9f;

        if (locateHandler == null) {
            return;
        }
        for (ControlAction action : actions) {
            action.update(locateHandler);
        }

    }

    public ControlAction currentAction() {
        return actions[current];
    }


    public void handleMouseInput(IMultiblockLocateHandler locateHandler, int mouseButton, boolean pressed) {
        if (currentAction().isEnabled())
            currentAction().handleMouseInput(locateHandler, mouseButton, pressed);
    }

    public void handleKeyInput(IMultiblockLocateHandler locateHandler, int key, boolean pressed) {
        if (key == KeysRegistry.TOOL_CTRL_KEY.getKey().getValue()) {
            focused = pressed;
        }
        if (currentAction().isEnabled())
            currentAction().handleKeyInput(locateHandler, key, pressed);
    }

    public boolean handleMouseScrolled(IMultiblockLocateHandler locateHandler, double delta) {
        if (KeysRegistry.TOOL_CTRL_KEY.isDown()) {
            cycleAction((int) delta);
            return true;
        }
        if (currentAction().isEnabled())
            return currentAction().handleMouseScrolled(locateHandler, delta);
        return false;
    }

    public void cycleAction(int direction) {
        current += (direction < 0) ? 1 : -1;
        current = (current + actions.length) % actions.length;
    }


    @Override
    protected void init() {
        super.init();
        initialized = true;
    }
}
