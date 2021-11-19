package com.nmmoc7.polymercore.client.resources;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nmmoc7.polymercore.client.utils.Color;
import net.minecraft.client.gui.AbstractGui;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IGuiResource {
    @OnlyIn(Dist.CLIENT)
    void bind();

    @OnlyIn(Dist.CLIENT)
    void draw(MatrixStack ms, AbstractGui screen, int x, int y);

    @OnlyIn(Dist.CLIENT)
    void draw(MatrixStack ms, int zLevel, int x, int y, Color c);


    IGuiResource EMPTY = new IGuiResource() {
        @Override
        public void bind() {

        }

        @Override
        public void draw(MatrixStack ms, AbstractGui screen, int x, int y) {

        }

        @Override
        public void draw(MatrixStack ms, int zLevel, int x, int y, Color c) {

        }
    };
}
