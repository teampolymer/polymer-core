package com.nmmoc7.polymercore.common.capability.blueprint;

import com.nmmoc7.polymercore.api.capability.IMultiblockLocateHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class MultiblockLocateItemStack implements IMultiblockLocateHandler {
    public MultiblockLocateItemStack(ItemStack itemStack) {
        this.stack = itemStack;
    }

    private final ItemStack stack;

    @Override
    public boolean isAnchored() {
        return stack.getOrCreateTag().getBoolean("anchored");
    }

    @Override
    public void setAnchored(boolean anchored) {
        stack.getOrCreateTag().putBoolean("anchored", anchored);
    }


    @Override
    public BlockPos getOffset() {
        return NBTUtil.readBlockPos(stack.getOrCreateChildTag("offset"));
    }

    @Override
    public void setOffset(BlockPos offset) {
        stack.getOrCreateTag().put("offset", NBTUtil.writeBlockPos(offset));
    }

    @Override
    public Rotation getRotation() {
        byte rotation = stack.getOrCreateTag().getByte("rotation");
        return Rotation.values()[(rotation % 4 + 4) % 4];
    }

    @Override
    public void setRotation(Rotation rotation) {
        stack.getOrCreateTag().putByte("rotation", (byte) rotation.ordinal());
    }

    @Override
    public boolean isFlipped() {
        return stack.getOrCreateTag().getBoolean("flipped");
    }

    @Override
    public void setFlipped(boolean flipped) {
        stack.getOrCreateTag().putBoolean("flipped", flipped);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultiblockLocateItemStack that = (MultiblockLocateItemStack) o;
        return ItemStack.areItemsEqual(stack, that.stack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stack);
    }
}
