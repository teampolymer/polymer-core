package com.nmmoc7.polymercore.api.multiblock;

import com.nmmoc7.polymercore.api.multiblock.assembled.IMultiblockAssembleRule;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
import com.nmmoc7.polymercore.api.multiblock.part.IPartLimitConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

public interface IDefinedMultiblock extends IMultiblock {
    /**
     * 尝试组装一个多方快结构，并返回组装后的多方快
     *
     * @param coreOffset       核心方块坐标
     * @param rotation      多方快朝向
     * @param isSymmetrical 是否是和定义对称
     * @return 组装后的的多方块
     */
    @Nullable
    IAssembledMultiblock assemble(@NotNull World world, @NotNull BlockPos coreOffset, @NotNull Rotation rotation, boolean isSymmetrical);

    @Nullable
    IAssembledMultiblock assemble(@NotNull World world, @NotNull BlockPos coreOffset, @NotNull Rotation rotation);

    @Nullable
    IAssembledMultiblock assemble(@NotNull World world, @NotNull BlockPos coreOffset);

    /**
     * 判断一个多方快结构是否可以在指定位置组装, 并填充组装规则
     *
     * @param coreOffset       核心方块坐标
     * @param rotation      朝向
     * @param isSymmetrical 是否是和定义对称
     * @param ruleToFill    要填充的组装规则
     * @return 是否可以组装
     */
    boolean canAssemble(@NotNull World world, @NotNull BlockPos coreOffset, @NotNull Rotation rotation, boolean isSymmetrical, IMultiblockAssembleRule ruleToFill);

    /**
     * 判断一个多方快结构是否可以在指定位置组装
     *
     * @param coreOffset       核心方块坐标
     * @param rotation      朝向
     * @param isSymmetrical 是否是和定义对称
     * @return 是否可以组装
     */
    boolean canAssemble(@NotNull World world, @NotNull BlockPos coreOffset, @NotNull Rotation rotation, boolean isSymmetrical);

    boolean canAssemble(@NotNull World world, @NotNull BlockPos coreOffset, @NotNull Rotation rotation);

    boolean canAssemble(@NotNull World world, @NotNull BlockPos coreOffset);

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
    IMultiblockType getType();

    /**
     * 获取多方快结构的具体部件
     *
     * @return 多方快结构的部件
     */
    Map<Vector3i, IMultiblockPart> getParts();

    /**
     * 获取多方快结构的Tags，用于搜索
     *
     * @return string tag
     */
    Collection<String> getTags();

    /**
     * 获取多方快结构部件限制
     */
    Collection<IPartLimitConfig> getLimitConfigs();

    IDefinedMultiblock setRegistryName(ResourceLocation name);

    @Nullable
    ResourceLocation getRegistryName();


}
