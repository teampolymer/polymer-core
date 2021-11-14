package com.nmmoc7.polymercore.common.item;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.api.capability.IMultiblockLocateHandler;
import com.nmmoc7.polymercore.api.capability.IMultiblockSupplier;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.common.PolymerItemGroup;
import com.nmmoc7.polymercore.common.capability.blueprint.CapabilityMultiblock;
import com.nmmoc7.polymercore.common.capability.blueprint.MultiblockLocateItemStack;
import com.nmmoc7.polymercore.common.capability.blueprint.MultiblockSupplierItemStack;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlueprintItem extends Item {
    public BlueprintItem() {
        super(new Properties().group(PolymerItemGroup.INSTANCE));
    }

    public final String UNKNOWN_MULTIBLOCK = "name.polymer.multiblock.internal.unknown";
    public final String NAME_MULTIBLOCK_PREFIX = "name.polymer.multiblock.";

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ICapabilityProvider() {
            final LazyOptional<IMultiblockSupplier> SUPPLIER = LazyOptional.of(() -> new MultiblockSupplierItemStack(stack));
            final LazyOptional<IMultiblockLocateHandler> LOCATOR = LazyOptional.of(() -> new MultiblockLocateItemStack(stack));

            @NotNull
            @Override
            public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                if (CapabilityMultiblock.MULTIBLOCK_SUPPLIER == cap) {
                    return SUPPLIER.cast();
                }
                if (CapabilityMultiblock.MULTIBLOCK_LOCATE_HANDLER == cap) {
                    return LOCATOR.cast();
                }
                return LazyOptional.empty();
            }
        };
    }

    public IDefinedMultiblock getMultiblock(ItemStack stack) {
        LazyOptional<IMultiblockSupplier> capability = stack.getCapability(CapabilityMultiblock.MULTIBLOCK_SUPPLIER);
        if (!capability.isPresent()) {
            return null;
        }
        return capability.resolve().get().getMultiblock();
    }

    public String getMultiblockName(ItemStack stack) {
        IDefinedMultiblock multiblock = getMultiblock(stack);
        if (multiblock == null) {
            return I18n.format(UNKNOWN_MULTIBLOCK);
        }
        ResourceLocation registryName = multiblock.getRegistryName();
        if (registryName == null) {
            return I18n.format(UNKNOWN_MULTIBLOCK);
        }
        return I18n.format(NAME_MULTIBLOCK_PREFIX + registryName.getNamespace() + "." + registryName.getPath());
    }

    @Override
    public @NotNull ITextComponent getDisplayName(ItemStack stack) {
        return new TranslationTextComponent(this.getTranslationKey(stack), getMultiblockName(stack));
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            for (IDefinedMultiblock multiblock : PolymerCoreApi.getInstance()
                .getMultiblockManager()
                .findAll()) {
                ItemStack stack = new ItemStack(this);
                LazyOptional<IMultiblockSupplier> capability = stack.getCapability(CapabilityMultiblock.MULTIBLOCK_SUPPLIER);
                capability.ifPresent(it -> {
                    if (it instanceof IMultiblockSupplier.Mutable) {
                        ((IMultiblockSupplier.Mutable) it).setMultiblockRegistryName(multiblock.getRegistryName());
                    }
                    items.add(stack);
                });
            }
        }
    }
}
