package com.teampolymer.polymer.core.api.multiblock.builder;


import com.teampolymer.polymer.core.api.multiblock.part.IMultiblockPart;
import net.minecraft.util.math.vector.Vector3i;

/**
 * 用字符数组定义的多方快结构
 */
public interface IPartListMultiblockBuilder extends IMultiblockBuilder<IPartListMultiblockBuilder> {

    IPartListMultiblockBuilder part(Vector3i offset, IMultiblockPart part);
    IPartListMultiblockBuilder part(int x, int y, int z, IMultiblockPart part);
}
