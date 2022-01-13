package com.nmmoc7.polymercore.common.loot.modifiers;

import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * The smelting enchantment causes this modifier to be invoked, via the smelting loot_modifier json
 */
public class SmeltingEnchantmentModifier extends LootModifier {
    public SmeltingEnchantmentModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        ArrayList<ItemStack> ret = new ArrayList<>();
        generatedLoot.forEach((stack) -> ret.add(smelt(stack, context)));
        return ret;
    }

    private static ItemStack smelt(ItemStack stack, LootContext context) {
        return context.getLevel().getRecipeManager().getRecipeFor(IRecipeType.SMELTING, new Inventory(stack), context.getLevel())
            .map(FurnaceRecipe::getResultItem)
            .filter(itemStack -> !itemStack.isEmpty())
            .map(itemStack -> ItemHandlerHelper.copyStackWithSize(itemStack, stack.getCount() * itemStack.getCount()))
            .orElse(stack);
    }

    public static class Serializer extends GlobalLootModifierSerializer<SmeltingEnchantmentModifier> {
        @Override
        public SmeltingEnchantmentModifier read(ResourceLocation name, JsonObject json, ILootCondition[] conditionsIn) {
            return new SmeltingEnchantmentModifier(conditionsIn);
        }

        @Override
        public JsonObject write(SmeltingEnchantmentModifier instance) {
            return makeConditions(instance.conditions);
        }
    }
}
