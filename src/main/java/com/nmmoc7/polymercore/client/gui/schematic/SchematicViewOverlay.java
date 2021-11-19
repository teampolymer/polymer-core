package com.nmmoc7.polymercore.client.gui.schematic;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nmmoc7.polymercore.api.capability.IMultiblockLocateHandler;
import com.nmmoc7.polymercore.client.utils.schematic.control.*;
import com.nmmoc7.polymercore.common.registry.KeysRegistry;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;

import java.util.List;
import java.util.Locale;

/**
 * 蓝图控制的窗口
 */
public class SchematicViewOverlay extends Screen {

    public final String scrollToCycle = I18n.format("gui.polymer.toolmenu.cycle");
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


        w = Math.max(actions.length * 50 + 30, 220);
        h = 30;
    }

    private boolean initialized = false;

    public void renderOverlay(MatrixStack ms, float pt) {
        Minecraft mc = Minecraft.getInstance();
        MainWindow mainWindow = mc.getMainWindow();
        if (!initialized)
            init(mc, mainWindow.getScaledWidth(), mainWindow.getScaledHeight());

        int x = (mainWindow.getScaledWidth() - w) / 2 + 15;
        int y = mainWindow.getScaledHeight() - h - 75;

        ms.push();
        ms.translate(0, -yOffset, focused ? 100 : 0);

//        AllGuiTextures gray = AllGuiTextures.HUD_BACKGROUND;
//        RenderSystem.enableBlend();
//        RenderSystem.color4f(1, 1, 1, focused ? 7 / 8f : 1 / 2f);
//
//        Minecraft.getInstance()
//            .getTextureManager()
//            .bindTexture(gray.location);
//        blit(ms, x - 15, y, gray.startX, gray.startY, w, h, gray.width, gray.height);
//
//        float toolTipAlpha = yOffset / 10;
//        List<ITextComponent> toolTip = currentAction().getDescription();
//        int stringAlphaComponent = ((int) (toolTipAlpha * 0xFF)) << 24;
//
//        if (toolTipAlpha > 0.25f) {
//            RenderSystem.color4f(.7f, .7f, .8f, toolTipAlpha);
//            blit(ms, x - 15, y + 33, gray.startX, gray.startY, w, h + 22, gray.width, gray.height);
//            RenderSystem.color4f(1, 1, 1, 1);
//
//            if (toolTip.size() > 0)
//                font.drawText(ms, toolTip.get(0), x - 10, y + 38, 0xEEEEEE + stringAlphaComponent);
//            if (toolTip.size() > 1)
//                font.drawText(ms, toolTip.get(1), x - 10, y + 50, 0xCCDDFF + stringAlphaComponent);
//            if (toolTip.size() > 2)
//                font.drawText(ms, toolTip.get(2), x - 10, y + 60, 0xCCDDFF + stringAlphaComponent);
//            if (toolTip.size() > 3)
//                font.drawText(ms, toolTip.get(3), x - 10, y + 72, 0xCCCCDD + stringAlphaComponent);
//        }

        RenderSystem.color4f(1, 1, 1, 1);
        if (actions.length > 1) {

            String keyName = KeysRegistry.TOOL_CTRL_KEY.func_238171_j_().getString().toUpperCase(Locale.ROOT);
            int width = minecraft.getMainWindow()
                .getScaledWidth();
            if (!focused)
                drawCenteredString(ms, font, I18n.format(holdToFocus, keyName), width / 2,
                    y - 10, 0xCCDDFF);
            else
                drawCenteredString(ms, font, scrollToCycle, width / 2, y - 10, 0xCCDDFF);
        } else {
            x += 65;
        }

        for (int i = 0; i < actions.length; i++) {
            ms.push();

            float alpha = focused ? 1 : .2f;
            if (i == current) {
                ms.translate(0, -10, 0);
                drawCenteredString(ms, font, actions[i]
                    .getName(), x + i * 50 + 24, y + 28, 0xCCDDFF);
                alpha = 1;
            }
            RenderSystem.color4f(0, 0, 0, alpha);
            actions[i]
                .getIcon()
                .draw(ms, this, x + i * 50 + 16, y + 12);
            RenderSystem.color4f(1, 1, 1, alpha);
            actions[i]
                .getIcon()
                .draw(ms, this, x + i * 50 + 16, y + 11);

            ms.pop();
        }

        RenderSystem.enableBlend();
        ms.pop();
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
        currentAction().handleMouseInput(locateHandler, mouseButton, pressed);
    }

    public void handleKeyInput(IMultiblockLocateHandler locateHandler, int key, boolean pressed) {
        if (key == KeysRegistry.TOOL_CTRL_KEY.getKey().getKeyCode()) {
            focused = pressed;
        }
        currentAction().handleKeyInput(locateHandler, key, pressed);
    }

    public boolean handleMouseScrolled(IMultiblockLocateHandler locateHandler, double delta) {
        if (KeysRegistry.TOOL_CTRL_KEY.isKeyDown()) {
            cycleAction((int) delta);
            return true;
        }
        return currentAction().handleMouseScrolled(locateHandler, delta);
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
