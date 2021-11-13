package com.nmmoc7.polymercore.api.multiblock.assembled;

import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
import com.nmmoc7.polymercore.api.multiblock.part.IPartChoice;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;

/**
 * 多方快结构的组装规则
 */
public interface IMultiblockAssembleRule extends INBTSerializable<CompoundNBT> {
    /**
     * 获取方块结构的偏移量
     *
     * @return 偏移量
     */
    BlockPos getOffset();

    /**
     * 这个结构是否是对称的
     * 和 getRotation() 同时使用，表示结构和组装前结构相关的旋转方向
     */
    boolean isSymmetrical();

    /**
     * 获取方块结构旋转
     *
     * @return 旋转
     */
    Rotation getRotation();

    Map<BlockPos, IMultiblockUnit> mapParts(IDefinedMultiblock originalMultiblock);

    void makeChoice(Vector3i key, IPartChoice choice);

    String getChoiceType(Vector3i relativePos);
}
