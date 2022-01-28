package com.nmmoc7.polymercore.common.item;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.api.capability.IMultiblockLocateHandler;
import com.nmmoc7.polymercore.api.capability.IMultiblockSupplier;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.common.PolymerItemGroup;
import com.nmmoc7.polymercore.common.capability.blueprint.CapabilityMultiblockItem;
import com.nmmoc7.polymercore.common.capability.blueprint.MultiblockLocateItemStack;
import com.nmmoc7.polymercore.common.capability.blueprint.MultiblockSupplierItemStack;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.item.Item.Properties;

public class BlueprintItem extends Item {
    public BlueprintItem() {
        super(new Properties().tab(PolymerItemGroup.INSTANCE));
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
                if (CapabilityMultiblockItem.MULTIBLOCK_SUPPLIER == cap) {
                    return SUPPLIER.cast();
                }
                if (CapabilityMultiblockItem.MULTIBLOCK_LOCATE_HANDLER == cap) {
                    return LOCATOR.cast();
                }
                return LazyOptional.empty();
            }
        };
    }

    @Override
    public @NotNull ActionResult<ItemStack> use(@NotNull World world, PlayerEntity player, @NotNull Hand hand) {
        return ActionResult.success(player.getItemInHand(hand));
    }

    public IDefinedMultiblock getMultiblock(ItemStack stack) {
        LazyOptional<IMultiblockSupplier> capability = stack.getCapability(CapabilityMultiblockItem.MULTIBLOCK_SUPPLIER);
        if (!capability.isPresent()) {
            return null;
        }
        return capability.resolve().get().getMultiblock();
    }

    public String getMultiblockName(ItemStack stack) {
        IDefinedMultiblock multiblock = getMultiblock(stack);
        if (multiblock == null) {
            return I18n.get(UNKNOWN_MULTIBLOCK);
        }
        ResourceLocation registryName = multiblock.getRegistryName();
        if (registryName == null) {
            return I18n.get(UNKNOWN_MULTIBLOCK);
        }
        return I18n.get(NAME_MULTIBLOCK_PREFIX + registryName.getNamespace() + "." + registryName.getPath());
    }

    @Override
    public @NotNull ITextComponent getName(ItemStack stack) {
        return new TranslationTextComponent(this.getDescriptionId(stack), getMultiblockName(stack));
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.allowdedIn(group)) {
            for (IDefinedMultiblock multiblock : PolymerCoreApi.getInstance()
                .getMultiblockManager()
                .findAll()) {
                ItemStack stack = new ItemStack(this);
                LazyOptional<IMultiblockSupplier> capability = stack.getCapability(CapabilityMultiblockItem.MULTIBLOCK_SUPPLIER);
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
