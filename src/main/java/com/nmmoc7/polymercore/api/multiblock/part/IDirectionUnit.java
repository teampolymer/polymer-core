package com.nmmoc7.polymercore.api.multiblock.part;

import com.nmmoc7.polymercore.api.multiblock.MultiblockDirection;
import net.minecraft.util.Rotation;

/**
 * 有方向的单元，如楼梯等
 */
public interface IDirectionUnit extends IMultiblockUnit {
    default Rotation getRotation() {
        return getDirection().getRotation();
    }

    default boolean isFlipped() {
        return getDirection().isFlipped();
    }

    MultiblockDirection getDirection();

    IDirectionUnit withDirection(MultiblockDirection direction);

}
