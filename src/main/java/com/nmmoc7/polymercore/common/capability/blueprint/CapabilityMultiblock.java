package com.nmmoc7.polymercore.common.capability.blueprint;

import com.nmmoc7.polymercore.PolymerCore;
import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.api.capability.IChunkMultiblockStorage;
import com.nmmoc7.polymercore.api.capability.IMultiblockLocateHandler;
import com.nmmoc7.polymercore.api.capability.IMultiblockSupplier;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.common.capability.chunk.CapabilityChunkMultiblockStorage;
import net.minecraft.nbt.*;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.energy.EnergyStorage;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.MathUtil;

import java.util.List;
import java.util.Optional;

public class CapabilityMultiblock {
    @CapabilityInject(IMultiblockSupplier.class)
    public static Capability<IMultiblockSupplier> MULTIBLOCK_SUPPLIER = null;
    @CapabilityInject(IMultiblockSupplier.class)
    public static Capability<IMultiblockLocateHandler> MULTIBLOCK_LOCATE_HANDLER = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IMultiblockSupplier.class, new Capability.IStorage<IMultiblockSupplier>() {

                @Nullable
                @Override
                public INBT writeNBT(Capability<IMultiblockSupplier> capability, IMultiblockSupplier instance, Direction side) {
                    ResourceLocation registryName = instance.getRegistryName();
                    if (registryName == null) {
                        return null;
                    }
                    return StringNBT.valueOf(registryName.toString());
                }

                @Override
                public void readNBT(Capability<IMultiblockSupplier> capability, IMultiblockSupplier instance, Direction side, INBT nbt) {

                    if (!(instance instanceof IMultiblockSupplier.Mutable))
                        throw new IllegalArgumentException("Can not deserialize to an instance that isn't mutable implementation");

                    ((IMultiblockSupplier.Mutable) instance).setMultiblock(new ResourceLocation(nbt.getString()));
                }
            },
            () -> new MultiblockSupplier(null));

        CapabilityManager.INSTANCE.register(IMultiblockLocateHandler.class, new Capability.IStorage<IMultiblockLocateHandler>() {
                @Nullable
                @Override
                public INBT writeNBT(Capability<IMultiblockLocateHandler> capability, IMultiblockLocateHandler instance, Direction side) {
                    CompoundNBT result = new CompoundNBT();
                    result.put("offset", NBTUtil.writeBlockPos(instance.getOffset()));
                    result.putByte("rotation", (byte) instance.getRotation().ordinal());
                    result.putBoolean("flipped", instance.isFlipped());
                    result.putBoolean("anchored", instance.isAnchored());
                    return result;
                }

                @Override
                public void readNBT(Capability<IMultiblockLocateHandler> capability, IMultiblockLocateHandler instance, Direction side, INBT nbt) {
                    CompoundNBT compoundNBT = (CompoundNBT) nbt;
                    instance.setOffset(NBTUtil.readBlockPos(compoundNBT.getCompound("offset")));
                    byte rotation = compoundNBT.getByte("rotation");
                    instance.setRotation(Rotation.values()[(rotation % 4 + 4) % 4]);
                    instance.setFlipped(compoundNBT.getBoolean("flipped"));
                    instance.setAnchored(compoundNBT.getBoolean("anchored"));
                }
            },
            MultiblockLocateHandler::new);

    }

}
