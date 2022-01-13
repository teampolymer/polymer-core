package com.nmmoc7.polymercore.common.loot;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.common.loot.functions.SaveRestorableNbt;
import com.nmmoc7.polymercore.common.loot.modifiers.SmeltingEnchantmentModifier;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class LootRegistries {
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> GLM = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, PolymerCoreApi.MOD_ID);
    public static final RegistryObject<SmeltingEnchantmentModifier.Serializer> SMELTING = GLM.register("smelting", SmeltingEnchantmentModifier.Serializer::new);


    public static LootFunctionType SAVE_RESTORABLE_NBT;
    public static void init() {
        SAVE_RESTORABLE_NBT = Registry.register(Registry.LOOT_FUNCTION_TYPE,
            new ResourceLocation(PolymerCoreApi.MOD_ID, "save_restorable_nbt"),
            new LootFunctionType(new SaveRestorableNbt.Serializer()));
    }
}
