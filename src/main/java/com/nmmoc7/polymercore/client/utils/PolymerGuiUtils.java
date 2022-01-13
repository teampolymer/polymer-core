package com.nmmoc7.polymercore.client.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;
import org.lwjgl.opengl.GL11;

public class PolymerGuiUtils {
    public static void drawColoredTexturedModalRect(MatrixStack ms, Color c, int x, int y, int u, int v, int width, int height, float zLevel, int textureWidth, int textureHeight) {
        final float uScale = 1f / textureWidth;
        final float vScale = 1f / textureHeight;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder wr = tessellator.getBuilder();
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX);
        Matrix4f matrix = ms.last().pose();
        wr.vertex(matrix, x        , y + height, zLevel).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).uv( u          * uScale, ((v + height) * vScale)).endVertex();
        wr.vertex(matrix, x + width, y + height, zLevel).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).uv((u + width) * uScale, ((v + height) * vScale)).endVertex();
        wr.vertex(matrix, x + width, y         , zLevel).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).uv((u + width) * uScale, ( v           * vScale)).endVertex();
        wr.vertex(matrix, x        , y         , zLevel).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).uv( u          * uScale, ( v           * vScale)).endVertex();
        tessellator.end();
    }
}
