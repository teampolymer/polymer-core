package com.nmmoc7.polymercore.common.multiblock.assembled;

import com.nmmoc7.polymercore.api.multiblock.assembled.IMultiblockAssembleRule;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;


public abstract class AbstractAssembleRule implements IMultiblockAssembleRule {
    private BlockPos offset;
    private boolean isSymmetrical;
    private Rotation rotation;

    public AbstractAssembleRule() {

    }
    public AbstractAssembleRule(BlockPos offset, boolean isSymmetrical, Rotation rotation) {
        this.offset = offset;
        this.isSymmetrical = isSymmetrical;
        this.rotation = rotation;
    }

    @Override
    public BlockPos getOffset() {
        return offset;
    }

    @Override
    public boolean isSymmetrical() {
        return isSymmetrical;
    }

    @Override
    public Rotation getRotation() {
        return rotation;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("off", NBTUtil.writeBlockPos(offset));
        nbt.putBoolean("symm", isSymmetrical);
        nbt.putByte("rotation", (byte) rotation.ordinal());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.offset = NBTUtil.readBlockPos(nbt.getCompound("off"));
        this.isSymmetrical = nbt.getBoolean("symm");
        this.rotation = Rotation.values()[nbt.getByte("rotation")];
    }
}
