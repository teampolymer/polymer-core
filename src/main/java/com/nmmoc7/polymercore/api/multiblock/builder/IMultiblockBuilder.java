package com.nmmoc7.polymercore.api.multiblock.builder;

import com.nmmoc7.polymercore.api.component.IMultiblockComponent;
import com.nmmoc7.polymercore.api.machine.IMachine;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.IMultiblockType;
import com.nmmoc7.polymercore.api.multiblock.extension.IMultiblockExtension;

public interface IMultiblockBuilder {
    IDefinedMultiblock build();

    /**
     * 设置多方快结构绑定的机械
     */
    IMultiblockBuilder setMachine(IMachine machine);

    /**
     * 设置多方快结构类型
     */
    IMultiblockBuilder setType(IMultiblockType type);

    /**
     * 添加组件
     */
    IMultiblockBuilder addComponent(IMultiblockComponent component);

    /**
     * 添加组件
     */
    IMultiblockBuilder addComponents(IMultiblockComponent... component);

    /**
     * 添加拓展点
     */
    IMultiblockBuilder addExtension(IMultiblockExtension extension);

    /**
     * 设置多方快结构可以对称放置
     */
    default IMultiblockBuilder setCanSymmetrical() {
        return setCanSymmetrical(true);
    }

    /**
     * 设置多方快结构可以对称放置
     *
     * @param canSymmetrical 是否允许对称
     */
    IMultiblockBuilder setCanSymmetrical(boolean canSymmetrical);
    ICharMarkedMultiblockBuilder addTags(String... tags);

}
