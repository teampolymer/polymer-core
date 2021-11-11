package com.nmmoc7.polymercore.api.multiblock.extension;

import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Map;

public interface IMultiblockExtension extends IForgeRegistryEntry<IMultiblockExtension> {

    /**
     * 获取多方快结构的具体部件
     *
     * @return 多方快结构的部件
     */
    Map<Vector3i, IMultiblockUnit> getParts();

    /**
     * 获取多方快结构的拓展点
     *
     * @return 拓展点（相对坐标）
     */
    Vector3i getExtensionPoint();

    /**
     * 获取多方快结构拓展的方向
     * 获取多方快结构拓展的方向
     *
     * @return 拓展方向
     */
    Direction getExtensionDirection();

    /**
     * 获取本拓展的深度
     *
     * @return 深度
     */
    int getDepth();

    /**
     * 支持的最大拓展数量
     *
     * @return 最大拓展数量
     */
    int maxExtensionCount();
}
