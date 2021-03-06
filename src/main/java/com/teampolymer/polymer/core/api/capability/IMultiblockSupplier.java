package com.teampolymer.polymer.core.api.capability;

import com.teampolymer.polymer.core.api.PolymerCoreApi;
import com.teampolymer.polymer.core.api.multiblock.IArchetypeMultiblock;
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
    default IArchetypeMultiblock getMultiblock() {
        ResourceLocation registryName = getMultiblockRegistryName();
        if (registryName == null) {
            return null;
        }
        return PolymerCoreApi.getInstance()
            .getArchetypeManager()
            .findById(registryName)
            .orElse(null);
    }

    ResourceLocation getMultiblockRegistryName();

    interface Mutable extends IMultiblockSupplier {
        void setMultiblockRegistryName(ResourceLocation registryName);
    }
}
