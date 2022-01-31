package com.teampolymer.polymer.core.common.item;

import com.teampolymer.polymer.core.api.PolymerCoreApi;
import com.teampolymer.polymer.core.common.PolymerItemGroup;
import com.teampolymer.polymer.core.common.block.ModBlocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, PolymerCoreApi.MOD_ID);

    public static final RegistryObject<Item> BLUEPRINT = REGISTER.register("blueprint", BlueprintItem::new);
}
