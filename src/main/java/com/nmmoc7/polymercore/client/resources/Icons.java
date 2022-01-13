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

public enum Icons implements IGuiResource {
    ROTATE(0,0),
    FLIP(0,1),
    LOCATE(0,2),
    LOCATE_DYNAMIC(0,3),
    MOVE_X(0,4),
    MOVE_Y(0,5),
    MOVE_Z(0,6),
    LAYERS(0,7),
    LOCK(0,8),
    EXTENSION(0,9),
    HAMMER(0,10),
    ;

    Icons(int row, int column) {
        startX = column * 16;
        startY = row * 16;
    }

    public static final ResourceLocation location = new ResourceLocation(PolymerCoreApi.MOD_ID, "textures/misc/icons.png");
    public static final int width = 16, height = 16;

    @Override
    public ResourceLocation getLocation() {
        return location;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getStartX() {
        return startX;
    }

    @Override
    public int getStartY() {
        return startY;
    }

    public final int startX, startY;

    @OnlyIn(Dist.CLIENT)
    public void bind() {
        Minecraft.getInstance()
            .getTextureManager()
            .bind(location);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void draw(MatrixStack ms, int zLevel, int x, int y) {
        bind();
        AbstractGui.blit(ms, x, y, zLevel, startX, startY, width, height, 256, 256);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void draw(MatrixStack ms, int zLevel, int x, int y, Color c) {
        bind();
        PolymerGuiUtils.drawColoredTexturedModalRect(ms, c, x, y, startX, startY, width, height, zLevel, 256, 256);
    }
}
