package com.nmmoc7.polymercore.common.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.Nullable;

public class EmptyStorage<T> implements Capability.IStorage<T> {
    private static final EmptyStorage<?> _instance = new EmptyStorage<>();
    public static  <T> EmptyStorage<T> getInstance() {
        //noinspection unchecked
        return (EmptyStorage<T>) _instance;
    }
    @Nullable
    @Override
    public INBT writeNBT(Capability<T> capability, T instance, Direction side) {
        return null;
    }

    @Override
    public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt) {

    }
}
