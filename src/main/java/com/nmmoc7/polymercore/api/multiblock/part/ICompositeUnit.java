package com.nmmoc7.polymercore.api.multiblock.part;

import java.util.List;

/**
 * 组合单元，只能提供最低限度的组合
 * 仅仅为了组合不同种类的Unit
 * 为了性能考虑请尽量不要手动创建组合单元
 * 请使用Builder创建单元以最低限度的减少组合的使用，它会自动合并同类型的单元
 */
public interface ICompositeUnit extends IMultiblockUnit {
    List<IMultiblockUnit> getChildUnits();

    boolean isDirectionAware();
}
