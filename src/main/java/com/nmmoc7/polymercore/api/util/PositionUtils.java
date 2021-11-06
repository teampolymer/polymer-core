package com.nmmoc7.polymercore.api.util;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;

public class PositionUtils {

    /**
     * 旋转，对称，平移
     *
     * @param pos           相对坐标
     * @param offset        平移
     * @param rotation      旋转
     * @param isSymmetrical 对称
     * @return 处理后的坐标
     */
    public static BlockPos applyModifies(Vector3i pos, BlockPos offset, Rotation rotation, boolean isSymmetrical) {
        int y = offset.getY() + pos.getY();
        int x0 = offset.getX();
        int z0 = offset.getZ();
        switch (rotation) {
            case NONE:
            default:
                if (isSymmetrical)
                    return new BlockPos(-pos.getX() + x0, y, pos.getZ() + z0);
                return new BlockPos(pos.getX() + x0, y, pos.getZ() + z0);
            case CLOCKWISE_90:
                if (isSymmetrical)
                    return new BlockPos(-pos.getZ() + x0, y, -pos.getX() + z0);
                return new BlockPos(-pos.getZ() + x0, y, pos.getX() + z0);
            case CLOCKWISE_180:
                if (isSymmetrical)
                    return new BlockPos(pos.getX() + x0, y, -pos.getZ() + z0);
                return new BlockPos(-pos.getX() + x0, y, -pos.getZ() + z0);
            case COUNTERCLOCKWISE_90:
                if (isSymmetrical)
                    return new BlockPos(pos.getZ() + x0, y, pos.getX() + z0);
                return new BlockPos(pos.getZ() + x0, y, -pos.getX() + z0);
        }
    }
}
