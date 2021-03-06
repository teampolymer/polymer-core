package com.teampolymer.polymer.core.api.multiblock;

import com.teampolymer.polymer.core.api.multiblock.assembled.IMultiblockAssembleRule;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IMultiblockType extends IForgeRegistryEntry<IMultiblockType> {
    /**
     * 在指定坐标创建多方快结构，并进行一些额外操作（写ChunkData，替换方块等等）
     *
     * @param world        世界
     * @param assembleRule 组装的规则
     * @return 组装后的多方快结构
     */
    IAssembledMultiblock placeMultiblockIn(IArchetypeMultiblock definition, World world, IMultiblockAssembleRule assembleRule);


    IAssembledMultiblock createFromNBT(CompoundNBT nbt);

    IMultiblockAssembleRule createEmptyRule(BlockPos coreOffset, Rotation rotation, boolean isSymmetrical);
    IMultiblockAssembleRule createRuleFromNBT(CompoundNBT nbt);

}
