package com.nmmoc7.polymercore.api.util;

import com.nmmoc7.polymercore.api.multiblock.MultiblockDirection;
import net.minecraft.block.BlockState;
import net.minecraft.util.Mirror;

public class MultiblockPartUtils {
    public static BlockState withDirection(BlockState origin, MultiblockDirection direction) {
        return origin
            .rotate(direction.getRotation())
            .mirror(direction.isFlipped() ? Mirror.FRONT_BACK : Mirror.NONE);
    }
}
