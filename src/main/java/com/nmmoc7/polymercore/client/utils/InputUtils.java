package com.nmmoc7.polymercore.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;

public class InputUtils {

    public static BlockPos getHoveringPos() {
        //获取玩家正在看向的方块
        if (Minecraft.getInstance().hitResult instanceof BlockRayTraceResult) {
            BlockRayTraceResult rayTraceResult = (BlockRayTraceResult) Minecraft.getInstance().hitResult;
            if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
                return rayTraceResult.getBlockPos().relative(rayTraceResult.getDirection());
            }
        }
        return null;
    }
}
