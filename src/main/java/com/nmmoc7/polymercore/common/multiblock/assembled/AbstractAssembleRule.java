package com.nmmoc7.polymercore.common.multiblock.assembled;

import com.nmmoc7.polymercore.api.multiblock.assembled.IMultiblockAssembleRule;
import com.nmmoc7.polymercore.api.multiblock.part.IPartChoice;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;

import java.util.HashMap;
import java.util.Map;


public abstract class AbstractAssembleRule implements IMultiblockAssembleRule {
    private BlockPos offset;
    private boolean isSymmetrical;
    private Rotation rotation;
    protected Map<Vector3i, String> choices;

    public AbstractAssembleRule() {
        this.choices = new HashMap<>();
    }

    public AbstractAssembleRule(BlockPos offset, boolean isSymmetrical, Rotation rotation) {
        this.offset = offset;
        this.isSymmetrical = isSymmetrical;
        this.rotation = rotation;
        this.choices = new HashMap<>();
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
    public void makeChoice(Vector3i key, IPartChoice choice) {
        choices.put(key, choice.getType());
    }

    @Override
    public String getChoiceType(Vector3i relativePos) {
        return choices.get(relativePos);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("off", NBTUtil.writeBlockPos(offset));
        nbt.putBoolean("symm", isSymmetrical);
        nbt.putByte("rotation", (byte) rotation.ordinal());
        ListNBT choicesNbt = new ListNBT();
        for (Map.Entry<Vector3i, String> entry : choices.entrySet()) {
            long longKey = BlockPos.asLong(entry.getKey().getX(), entry.getKey().getY(), entry.getKey().getZ());
            CompoundNBT nbtChoice = new CompoundNBT();
            nbtChoice.putLong("pos", longKey);
            nbtChoice.putString("type", entry.getValue());
            choicesNbt.add(nbtChoice);
        }
        nbt.put("choices", choicesNbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.offset = NBTUtil.readBlockPos(nbt.getCompound("off"));
        this.isSymmetrical = nbt.getBoolean("symm");
        this.rotation = Rotation.values()[nbt.getByte("rotation")];

        ListNBT choicesNbt = nbt.getList("choices", 10);
        for (INBT inbt : choicesNbt) {
            if (!(inbt instanceof CompoundNBT))
                continue;
            CompoundNBT nbtChoice = (CompoundNBT) inbt;
            long key = nbtChoice.getLong("pos");
            String val = nbtChoice.getString("type");
            choices.put(new Vector3i(BlockPos.getX(key), BlockPos.getY(key), BlockPos.getZ(key)), val);
        }
    }
}
