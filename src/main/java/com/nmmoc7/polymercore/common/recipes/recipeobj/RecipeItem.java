package com.nmmoc7.polymercore.common.recipes.recipeobj;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;

public class RecipeItem extends RecipeObject<Item> {
    public RecipeItem(Item obj) {
        super(obj);
    }

    @Override
    public Ingredient getIngredient() {
        return Ingredient.of(obj);
    }

    @Override
    public boolean matches(ItemStack obj) {
        return this.obj == obj.getItem();
    }

    @Override
    public JsonObject toJson() {
        return new Ingredient.SingleItemList(new ItemStack(obj)).serialize();
    }

    @Override
    public Item fromJson(JsonObject json) {
        return Ingredient.valueFromJson(json).getItems().toArray(new ItemStack[0])[0].getItem();
    }

    @Override
    public CompoundNBT toNBT() {
        CompoundNBT nbt = new CompoundNBT();
        new ItemStack(obj).save(nbt);
        return nbt;
    }

    @Override
    public Item fromNBT(CompoundNBT nbt) {
        return ItemStack.of(nbt).getItem();
    }

    @Override
    public RecipeObjectType<Item, ? extends RecipeObject<Item>> getType() {
        return RecipeObjectRegisterHandler.ITEM;
    }
}
