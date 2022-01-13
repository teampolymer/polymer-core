package com.nmmoc7.polymercore.common.recipes.recipeobj;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;

public class RecipeItemStack extends RecipeObject<ItemStack> {
    public RecipeItemStack(ItemStack obj) {
        super(obj);
    }

    @Override
    public Ingredient getIngredient() {
        return Ingredient.of(obj);
    }

    @Override
    public boolean matches(ItemStack obj) {
        return ItemStack.matches(this.obj, obj);
    }

    @Override
    public JsonObject toJson() {
        return new Ingredient.SingleItemList(obj).serialize();
    }

    @Override
    public ItemStack fromJson(JsonObject json) {
        return Ingredient.valueFromJson(json).getItems().toArray(new ItemStack[0])[0];
    }

    @Override
    public CompoundNBT toNBT() {
        CompoundNBT nbt = new CompoundNBT();
        obj.save(nbt);
        return nbt;
    }

    @Override
    public ItemStack fromNBT(CompoundNBT nbt) {
        return ItemStack.of(nbt);
    }

    @Override
    public RecipeObjectType<ItemStack, ? extends RecipeObject<ItemStack>> getType() {
        return RecipeObjectRegisterHandler.ITEM_STACK;
    }
}
