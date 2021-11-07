package com.nmmoc7.polymercore.common.capability.blueprint;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.api.capability.IMultiblockSupplier;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

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
