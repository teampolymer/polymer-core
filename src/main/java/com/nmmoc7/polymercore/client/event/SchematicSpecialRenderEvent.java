package com.nmmoc7.polymercore.client.event;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.eventbus.api.Event;

public class SchematicSpecialRenderEvent extends Event {
    public final BlockState state;
    public final IRenderTypeBuffer bufferSource;
    public final MatrixStack ms;
    public final boolean isDynamic;
    public final int combinedLight;

    @Override
    public boolean isCancelable() {
        return true;
    }

    public SchematicSpecialRenderEvent(BlockState state, IRenderTypeBuffer bufferSource, MatrixStack ms, boolean isDynamic, int combinedLight) {
        this.state = state;
        this.bufferSource = bufferSource;
        this.ms = ms;
        this.isDynamic = isDynamic;
        this.combinedLight = combinedLight;
    }
}
