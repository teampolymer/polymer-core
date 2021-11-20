package com.nmmoc7.polymercore.client.resources;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nmmoc7.polymercore.client.utils.Color;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IGuiResource {
    ResourceLocation getLocation();

    int getWidth();

    int getHeight();

    int getStartX();

    int getStartY();

    @OnlyIn(Dist.CLIENT)
    void bind();

    @OnlyIn(Dist.CLIENT)
    void draw(MatrixStack ms, int zLevel, int x, int y);

    @OnlyIn(Dist.CLIENT)
    void draw(MatrixStack ms, int zLevel, int x, int y, Color c);


    IGuiResource EMPTY = new IGuiResource() {
        @Override
        public ResourceLocation getLocation() {
            return null;
        }

        @Override
        public int getWidth() {
            return 0;
        }

        @Override
        public int getHeight() {
            return 0;
        }

        @Override
        public int getStartX() {
            return 0;
        }

        @Override
        public int getStartY() {
            return 0;
        }

        @Override
        public void bind() {

        }

        @Override
        public void draw(MatrixStack ms, int zLevel, int x, int y) {

        }

        @Override
        public void draw(MatrixStack ms, int zLevel, int x, int y, Color c) {

        }
    };
}
