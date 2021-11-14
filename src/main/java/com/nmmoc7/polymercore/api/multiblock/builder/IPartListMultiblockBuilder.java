package com.nmmoc7.polymercore.api.multiblock.builder;


import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
import net.minecraft.util.math.vector.Vector3i;

import java.util.Map;

/**
 * 用字符数组定义的多方快结构
 */
public interface IPartListMultiblockBuilder extends IMultiblockBuilder<IPartListMultiblockBuilder> {

    IPartListMultiblockBuilder part(Vector3i offset, IMultiblockPart part);
    IPartListMultiblockBuilder part(int x, int y, int z, IMultiblockPart part);
}
