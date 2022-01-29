package com.teampolymer.polymer.core.api.multiblock.part;

import com.teampolymer.polymer.core.api.multiblock.MultiblockDirection;
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
