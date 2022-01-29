package com.teampolymer.polymer.core.common.capability.io;

import com.teampolymer.polymer.core.api.storage.io.IReplyContext;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class ItemStackReplyContext implements IReplyContext<ItemStack> {
    boolean success;
    ItemStack[] stacks;

    public ItemStackReplyContext(boolean success, ItemStack... stacks) {
        this.success = success;
        this.stacks = stacks;
    }

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public List<ItemStack> get() {
        return Arrays.asList(stacks);
    }
}
