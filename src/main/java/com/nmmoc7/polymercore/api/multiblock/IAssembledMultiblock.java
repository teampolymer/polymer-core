package com.nmmoc7.polymercore.api.multiblock;

import com.nmmoc7.polymercore.api.multiblock.assembled.IMultiblockAssembleRule;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;
import java.util.UUID;

/**
 * 组装后的多方快结构
 */
public interface IAssembledMultiblock extends INBTSerializable<CompoundNBT> {
    /**
     * 获取多方快结构的Id
     *
     * @return id
     */
    UUID getMultiblockId();

    /**
     * 取消组装本多方快
     */
    void disassemble(World world);

    /**
     * 初始化多方快结构
     */
    boolean initialize();

    /**
     * 获取这个多方快组装前的的结构
     *
     * @return 组装前的结构
     */
    IDefinedMultiblock getOriginalMultiblock();

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

    /**
     * 获取多方快结构中所有部件
     *
     * @return 所有部件
     */
    Map<BlockPos, IMultiblockUnit> getUnits();


    boolean validate(World world, boolean disassemble);

    /**
     * 获取多方快结构组装的规则
     */
    IMultiblockAssembleRule getAssembleRule();


}
