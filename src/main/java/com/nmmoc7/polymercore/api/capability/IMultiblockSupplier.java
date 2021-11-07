package com.nmmoc7.polymercore.api.capability;

import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import net.minecraft.util.ResourceLocation;

/**
 * 可以提供多方快定义的玩意，可以是蓝图、核心方块等等
 */
public interface IMultiblockSupplier {
    /**
     * 获取提供的多方快结构
     *
     * @return
     */
    IDefinedMultiblock getMultiblock();

    ResourceLocation getRegistryName();

    interface Mutable extends IMultiblockSupplier {
        void setMultiblock(ResourceLocation registryName);
    }
}
