package com.nmmoc7.polymercore.common.item;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, PolymerCoreApi.MOD_ID);


    public static final RegistryObject<Item> BLUEPRINT = REGISTER.register("blueprint", BlueprintItem::new);
}
