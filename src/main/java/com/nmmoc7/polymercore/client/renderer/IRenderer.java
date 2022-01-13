package com.nmmoc7.polymercore.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;

public interface IRenderer {
    default void renderTick(MatrixStack ms, CustomRenderTypeBuffer buffer, float pt) {
        if (!isEnabled()) {
            return;
        }
        ms.pushPose();
        doRender(ms, buffer, pt);
        ms.popPose();
    }

    boolean isEnabled();

    void doRender(MatrixStack ms, CustomRenderTypeBuffer buffer, float pt);

    void update();
}
