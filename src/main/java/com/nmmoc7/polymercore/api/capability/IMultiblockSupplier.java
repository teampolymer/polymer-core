package com.nmmoc7.polymercore.api.capability;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
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
    default IDefinedMultiblock getMultiblock() {
        ResourceLocation registryName = getMultiblockRegistryName();
        if (registryName == null) {
            return null;
        }
        return PolymerCoreApi.getInstance()
            .getMultiblockManager()
            .findById(registryName)
            .orElse(null);
    }

    ResourceLocation getMultiblockRegistryName();

    interface Mutable extends IMultiblockSupplier {
        void setMultiblockRegistryName(ResourceLocation registryName);
    }
}
