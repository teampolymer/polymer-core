package com.nmmoc7.polymercore.common.capability.blueprint;

import com.nmmoc7.polymercore.api.capability.IMultiblockLocateHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class MultiblockLocateItemStack implements IMultiblockLocateHandler {
    public MultiblockLocateItemStack(ItemStack itemStack) {
        this.stack = itemStack;
    }

    private final ItemStack stack;

    private CompoundNBT getOrCreateSettingTag() {
        return stack.getOrCreateTagElement("locate");
    }

    @Override
    public boolean isAnchored() {
        return getOrCreateSettingTag().getBoolean("anchored");
    }

    @Override
    public void setAnchored(boolean anchored) {
        getOrCreateSettingTag().putBoolean("anchored", anchored);
    }


    @Override
    public BlockPos getOffset() {
        INBT offset = getOrCreateSettingTag().get("offset");
        if (offset instanceof CompoundNBT) {
            return NBTUtil.readBlockPos((CompoundNBT) offset);
        }
        return BlockPos.ZERO;
    }

    @Override
    public void setOffset(BlockPos offset) {
        if (offset == null) {
            offset = BlockPos.ZERO;
        }
        getOrCreateSettingTag().put("offset", NBTUtil.writeBlockPos(offset));
    }

    @Override
    public Rotation getRotation() {
        byte rotation = getOrCreateSettingTag().getByte("rotation");
        return Rotation.values()[(rotation % 4 + 4) % 4];
    }

    @Override
    public void setRotation(Rotation rotation) {
        getOrCreateSettingTag().putByte("rotation", (byte) rotation.ordinal());
    }

    @Override
    public boolean isFlipped() {
        return getOrCreateSettingTag().getBoolean("flipped");
    }

    @Override
    public void setFlipped(boolean flipped) {
        getOrCreateSettingTag().putBoolean("flipped", flipped);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultiblockLocateItemStack that = (MultiblockLocateItemStack) o;
        return ItemStack.tagMatches(stack, that.stack);
    }

    @Override
    public boolean equalsIgnoringSetting(IMultiblockLocateHandler other) {
        if (!(other instanceof MultiblockLocateItemStack)) {
            return false;
        }
        ItemStack stackA = this.stack;
        ItemStack stackB = ((MultiblockLocateItemStack) other).stack;
        if (stackA.isEmpty())
            return stackB.isEmpty();
        if (stackB.isEmpty())
            return false;
        CompoundNBT tagA = stackA.getTag();
        CompoundNBT tagB = stackB.getTag();
        if (tagA == null)
            return tagB == null;
        if (tagB == null)
            return false;

        if (tagA.size() != tagB.size()) {
            return false;
        }
        for (String key : tagA.getAllKeys()) {
            if ("locate".equals(key)) {
                continue;
            }
            INBT value = tagA.get(key);
            if (value == null) {
                if (!(tagB.get(key) == null && tagB.contains(key)))
                    return false;
            } else {
                if (!value.equals(tagB.get(key)))
                    return false;
            }
        }


        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stack);
    }
}
