package com.nmmoc7.polymercore.api.multiblock.builder;

import com.nmmoc7.polymercore.api.component.IMultiblockComponent;
import com.nmmoc7.polymercore.api.machine.IMachine;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.IMultiblockType;
import com.nmmoc7.polymercore.api.multiblock.extension.IMultiblockExtension;

public interface IMultiblockBuilder<T extends IMultiblockBuilder<T>> extends IBasicMultiblockBuilder {

    /**
     * 设置多方快结构绑定的机械
     */
    T machine(String machine);

    /**
     * 设置多方快结构类型
     */
    T type(IMultiblockType type);
    T type(String type);

    /**
     * 添加组件
     */
    T addComponent(IMultiblockComponent component);

    /**
     * 添加组件
     */
    T addComponents(IMultiblockComponent... component);

    /**
     * 添加拓展点
     */
    T addExtension(IMultiblockExtension extension);

    /**
     * 设置多方快结构可以对称放置
     */
    default T allowSymmetrical() {
        return allowSymmetrical(true);
    }

    /**
     * 设置多方快结构可以对称放置
     *
     * @param canSymmetrical 是否允许对称
     */
    T allowSymmetrical(boolean canSymmetrical);

    T addTags(String... tags);

    T limit(String partType, int min, int max);

    T limitMax(String partType, int max);

    T limitMin(String partType, int min);

}
