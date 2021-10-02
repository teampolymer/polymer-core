package com.nmmoc7.polymercore.api.multiblock;

import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Map;

public interface IDefinedMultiblock extends IForgeRegistryEntry<IDefinedMultiblock> {
    /**
     * 尝试组装一个多方快结构，并返回组装后的多方快
     *
     * @param corePos  核心方块坐标
     * @param rotation 多方快朝向
     * @return 组装后的的多方块
     */
    @Nullable
    IAssembledMultiblock assemble(BlockPos corePos, @Nullable Rotation rotation);

    @Nullable
    default IAssembledMultiblock assemble(BlockPos corePos) {
        return assemble(corePos, null);
    }

    /**
     * 判断一个多方快结构是否可以在指定位置组装
     *
     * @param corePos  核心方块坐标
     * @param rotation 朝向
     * @return 是否可以组装
     */
    boolean canAssemble(BlockPos corePos, @Nullable Rotation rotation);

    default boolean canAssemble(BlockPos corePos) {
        return canAssemble(corePos, null);
    }

    /**
     * 这个结构是否可以对称
     *
     * @return 是否可以对称
     */
    boolean canSymmetrical();

    /**
     * 获取多方快结构的类型
     *
     * @return 多方快结构的类型
     */
    IMultiblockType<?> getType();

    /**
     * 获取多方快结构的具体部件
     *
     * @return 多方快结构的部件
     */
    Map<Vector3i, IMultiblockPart> getParts();


}
