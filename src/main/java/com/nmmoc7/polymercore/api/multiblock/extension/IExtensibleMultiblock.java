package com.nmmoc7.polymercore.api.multiblock.extension;

import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;

import java.util.Collection;

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
}
