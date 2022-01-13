package com.nmmoc7.polymercore.common.loot.functions;

import com.google.gson.*;
import com.nmmoc7.polymercore.common.IRestorableTileEntity;
import com.nmmoc7.polymercore.common.loot.LootRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

public class SaveRestorableNbt extends LootFunction {

    protected SaveRestorableNbt(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    //写入Nbt的主要实现
    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        TileEntity tileEntity = context.getParamOrNull(LootParameters.BLOCK_ENTITY);
        if (tileEntity instanceof IRestorableTileEntity) {
            CompoundNBT tagCompound = new CompoundNBT();
            ((IRestorableTileEntity) tileEntity).writeRestorableToNBT(tagCompound);
            stack.setTag(tagCompound);
        }
        return stack;
    }

    @Override
    public LootFunctionType getType() {
        return LootRegistries.SAVE_RESTORABLE_NBT;
    }

    //没有额外参数，可以直接这样创建一个默认的builder，如果方法有参数的话还需要自己实现一个Builder类
    public static LootFunction.Builder<?> builder() {
        return simpleBuilder(SaveRestorableNbt::new);
    }

    public static class Serializer extends LootFunction.Serializer<SaveRestorableNbt> {
        //我们的自定义LootFunction没有额外参数，所以序列化方法不需要重写，反序列化方法直接new一个对象就行
        public SaveRestorableNbt deserialize(JsonObject object, JsonDeserializationContext ctx, ILootCondition[] conditions) {
            return new SaveRestorableNbt(conditions);
        }
    }
}
