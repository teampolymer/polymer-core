package com.nmmoc7.polymercore.client.resources;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.client.utils.Color;
import com.nmmoc7.polymercore.client.utils.PolymerGuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public enum GuiResources implements IGuiResource {

    TOOLBAR_BACKGROUND("overlay.png", 0, 0, 16, 16),
    ;
    public static final int FONT_COLOR = 0x575F7A;


    public final ResourceLocation location;
    public int width, height;

    public ResourceLocation getLocation() {
        return location;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int startX, startY;

    GuiResources(String location, int width, int height) {
        this(location, 0, 0, width, height);
    }

    GuiResources(int startX, int startY) {
        this("icons.png", startX * 16, startY * 16, 16, 16);
    }

    GuiResources(String location, int startX, int startY, int width, int height) {
        this(PolymerCoreApi.MOD_ID, location, startX, startY, width, height);
    }

    GuiResources(String namespace, String location, int startX, int startY, int width, int height) {
        this.location = new ResourceLocation(namespace, "textures/gui/" + location);
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
    }

    @OnlyIn(Dist.CLIENT)
    public void bind() {
        Minecraft.getInstance()
            .getTextureManager()
            .bind(location);
    }

    @OnlyIn(Dist.CLIENT)
    public void draw(MatrixStack ms, int zLevel, int x, int y) {
        bind();
        AbstractGui.blit(ms, x, y, zLevel, startX, startY, width, height, 256, 256);
    }

    @OnlyIn(Dist.CLIENT)
    public void draw(MatrixStack ms, int zLevel, int x, int y, Color c) {
        bind();
        PolymerGuiUtils.drawColoredTexturedModalRect(ms, c, x, y, startX, startY, width, height, zLevel, 256, 256);
    }

}
