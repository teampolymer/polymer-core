package com.nmmoc7.polymercore.api.multiblock.extension;

import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.List;

/**
 * 表示一个可以拓展的多方快结构
 */
public interface IExtensibleMultiblock extends IDefinedMultiblock {
    /**
     * 获取所有的拓展点
     *
     * @return 拓展点
     */
    Collection<IMultiblockExtension> getExtensions();

    /**
     * 在指定位置查找多方快结构的拓展
     *
     * @param world         世界
     * @param pos           坐标
     * @param rotation      旋转
     * @param isSymmetrical 对称
     * @return 找到的拓展和它的数量
     */
    List<Tuple<IMultiblockExtension, Integer>> findExtensionsFor(World world, BlockPos pos, Rotation rotation, boolean isSymmetrical);
}
