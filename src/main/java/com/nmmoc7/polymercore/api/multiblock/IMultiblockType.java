package com.nmmoc7.polymercore.api.multiblock;

import com.nmmoc7.polymercore.api.multiblock.extension.IMultiblockExtension;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public interface IMultiblockType {
    /**
     * 在指定坐标创建多方快结构，并进行一些额外操作（写ChunkData，替换方块等等）
     *
     * @param world             世界
     * @param pos               核心坐标
     * @param rotation          旋转角度
     * @param isSymmetrical     是否与定义对称
     * @param appliedExtensions 存在的拓展
     * @return 组装后的多方快结构
     */
    IAssembledMultiblock createMultiblockIn(IDefinedMultiblock definition, World world, BlockPos pos, Rotation rotation, boolean isSymmetrical,
                                            List<Tuple<IMultiblockExtension, Integer>> appliedExtensions);

    IAssembledMultiblock createMultiblockIn(IDefinedMultiblock definition, World world, BlockPos pos, Rotation rotation, boolean isSymmetrical);


    IAssembledMultiblock createFromNBT(CompoundNBT nbt);

}
