package com.nmmoc7.polymercore.common.capability.io;

import com.nmmoc7.polymercore.api.storage.IPolymerStorageCapability;
import com.nmmoc7.polymercore.api.storage.io.IInputContext;
import com.nmmoc7.polymercore.api.storage.io.IReplyContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleItemCapability implements IPolymerStorageCapability<ItemStack, IInputContext<ItemStack>, ItemStackOutputContext, IReplyContext<ItemStack>> {
    Map<Item, Integer> items = new HashMap<>();

    @Override
    public IReplyContext<ItemStack> tryAdd(IInputContext<ItemStack> context) {
        List<ItemStack> list = context.get();
        list.forEach(itemStack -> {
            if (items.get(itemStack.getItem()) == null) {
                items.put(itemStack.getItem(), itemStack.getCount());
            }
            else {
                items.put(itemStack.getItem(), items.get(itemStack.getItem()) + itemStack.getCount());
            }
        });
        return new ItemStackReplyContext(true);
    }

    @Override
    public IReplyContext<ItemStack> tryGet(ItemStackOutputContext context) {
        Map<Item, Integer> map = context.get();
        if (map.entrySet().stream().noneMatch(entry -> items.get(entry.getKey()) < entry.getValue())) {
            ArrayList<ItemStack> reply = new ArrayList<>();
            map.forEach((item, integer) -> {
                items.put(item, items.get(item) - integer);
                reply.add(new ItemStack(item, integer));
            });
            return new ItemStackReplyContext(true, reply.toArray(new ItemStack[0]));
        }
        else {
            return new ItemStackReplyContext(false);
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT result = new CompoundNBT();
        ListNBT list = new ListNBT();
        items.forEach((item, integer) -> {
            CompoundNBT itemNBT = new CompoundNBT();
            itemNBT.putString("name", item.getRegistryName().toString());
            itemNBT.putInt("count", integer);
            list.add(itemNBT);
        });
        result.put("list", list);
        return result;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        items.clear();

        ListNBT list = nbt.getList("list", 10);
        list.forEach(itemNBT -> {
            CompoundNBT cItemNBT = (CompoundNBT) itemNBT;
            items.put(ForgeRegistries.ITEMS.getValue(new ResourceLocation(cItemNBT.getString("name"))), cItemNBT.getInt("count"));
        });
    }
}
