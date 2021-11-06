package com.nmmoc7.polymercore.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;

public class InputUtils {

    public static BlockPos getHoveringPos() {
        //获取玩家正在看向的方块
        if (Minecraft.getInstance().objectMouseOver instanceof BlockRayTraceResult) {
            BlockRayTraceResult rayTraceResult = (BlockRayTraceResult) Minecraft.getInstance().objectMouseOver;
            if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
                return rayTraceResult.getPos().offset(rayTraceResult.getFace());
            }
        }
        return null;
    }
}
