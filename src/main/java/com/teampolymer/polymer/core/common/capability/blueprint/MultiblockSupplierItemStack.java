package com.teampolymer.polymer.core.common.capability.blueprint;

import com.teampolymer.polymer.core.api.capability.IMultiblockSupplier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class MultiblockSupplierItemStack implements IMultiblockSupplier, IMultiblockSupplier.Mutable {
    public MultiblockSupplierItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    private final ItemStack itemStack;

    @Override
    public ResourceLocation getMultiblockRegistryName() {
        CompoundNBT tagCompound = itemStack.getOrCreateTag();
        String multiblock = tagCompound.getString("multiblock");
        return new ResourceLocation(multiblock);
    }

    @Override
    public void setMultiblockRegistryName(ResourceLocation registryName) {
        itemStack.getOrCreateTag().putString("multiblock", registryName.toString());
    }
}
