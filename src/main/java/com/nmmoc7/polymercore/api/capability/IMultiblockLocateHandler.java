package com.nmmoc7.polymercore.api.capability;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

/**
 * 可以保存多方快部署参数的玩意，比如蓝图或者投影仪之类的
 */
public interface IMultiblockLocateHandler {
    Rotation getRotation();

    BlockPos getOffset();

    boolean isFlipped();

    boolean isAnchored();

    void setRotation(Rotation rotation);

    void setOffset(BlockPos offset);

    void setFlipped(boolean flipped);

    default void flip() {
        setFlipped(!isFlipped());
    }

    void setAnchored(boolean anchored);

    boolean equalsIgnoringSetting(IMultiblockLocateHandler other);

    default void reset() {
        setRotation(Rotation.NONE);
        setAnchored(false);
        setFlipped(false);
        setOffset(null);
    }

}
