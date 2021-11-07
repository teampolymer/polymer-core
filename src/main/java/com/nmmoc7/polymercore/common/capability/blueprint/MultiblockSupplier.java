package com.nmmoc7.polymercore.common.capability.blueprint;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.api.capability.IMultiblockSupplier;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import net.minecraft.util.ResourceLocation;

public class MultiblockSupplier implements IMultiblockSupplier, IMultiblockSupplier.Mutable {
    public MultiblockSupplier(ResourceLocation registryName) {
        this.registryName = registryName;
    }

    private ResourceLocation registryName;

    @Override
    public IDefinedMultiblock getMultiblock() {
        return PolymerCoreApi.getInstance()
            .getMultiblockManager()
            .getDefinedMultiblock(registryName)
            .orElse(null);
    }

    @Override
    public ResourceLocation getRegistryName() {
        return registryName;
    }

    @Override
    public void setMultiblock(ResourceLocation registryName) {
        this.registryName = registryName;
    }
}
