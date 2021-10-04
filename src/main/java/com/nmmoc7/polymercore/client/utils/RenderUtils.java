package com.nmmoc7.polymercore.client.utils;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;

import java.util.function.Consumer;

public class RenderUtils {


    private static void line(IVertexBuilder builder, Matrix4f positionMatrix, BlockPos pos, Consumer<IVertexBuilder> extra, float dx1, float dy1, float dz1, float dx2, float dy2, float dz2) {
        builder.pos(positionMatrix, pos.getX() + dx1, pos.getY() + dy1, pos.getZ() + dz1);
        extra.accept(builder);
        builder.endVertex();
        builder.pos(positionMatrix, pos.getX() + dx2, pos.getY() + dy2, pos.getZ() + dz2);
        extra.accept(builder);
        builder.endVertex();
    }

    private static void outline(IVertexBuilder builder, Matrix4f positionMatrix, BlockPos pos, Consumer<IVertexBuilder> extra) {
        line(builder, positionMatrix, pos, extra, 0, 0, 0, 1, 0, 0);
        line(builder, positionMatrix, pos, extra, 0, 1, 0, 1, 1, 0);
        line(builder, positionMatrix, pos, extra, 0, 0, 1, 1, 0, 1);
        line(builder, positionMatrix, pos, extra, 0, 1, 1, 1, 1, 1);

        line(builder, positionMatrix, pos, extra, 0, 0, 0, 0, 0, 1);
        line(builder, positionMatrix, pos, extra, 1, 0, 0, 1, 0, 1);
        line(builder, positionMatrix, pos, extra, 0, 1, 0, 0, 1, 1);
        line(builder, positionMatrix, pos, extra, 1, 1, 0, 1, 1, 1);

        line(builder, positionMatrix, pos, extra, 0, 0, 0, 0, 1, 0);
        line(builder, positionMatrix, pos, extra, 1, 0, 0, 1, 1, 0);
        line(builder, positionMatrix, pos, extra, 0, 0, 1, 0, 1, 1);
        line(builder, positionMatrix, pos, extra, 1, 0, 1, 1, 1, 1);
    }

    public static void outlineRed(IVertexBuilder builder, Matrix4f positionMatrix, BlockPos pos) {
        outline(builder, positionMatrix, pos, it -> it.color(1.0f, 0.2f, 0.1f, 1.0f));
    }
    public static void outlineGreen(IVertexBuilder builder, Matrix4f positionMatrix, BlockPos pos) {
        outline(builder, positionMatrix, pos, it -> it.color(0.15f, 1.0f, 0.1f, 1.0f));
    }


}
