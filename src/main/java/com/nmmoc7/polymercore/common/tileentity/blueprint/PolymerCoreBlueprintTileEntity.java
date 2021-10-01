package com.nmmoc7.polymercore.common.tileentity.blueprint;

import com.nmmoc7.polymercore.api.blueprint.IBlueprint;
import com.nmmoc7.polymercore.api.blueprint.type.IBlueprintType;
import com.nmmoc7.polymercore.api.tileentity.IPolymerCoreBlueprintTileEntity;
import com.nmmoc7.polymercore.common.RegisterHandler;
import com.nmmoc7.polymercore.common.blueprint.BlueprintRegisterHandler;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Map;

public class PolymerCoreBlueprintTileEntity extends IPolymerCoreBlueprintTileEntity {
    IBlueprint blueprint;

    public PolymerCoreBlueprintTileEntity() {
        super(RegisterHandler.BLUEPRINT_TILE);
    }

    @Override
    public void setBlueprint(IBlueprint blueprint) {
        this.blueprint = blueprint;
    }

    @Override
    public IBlueprint getBlueprint() {
        return blueprint;
    }

    @Override
    public boolean test() {
        if (getBlueprint() == null)
            return false;

        for (Map.Entry<BlockPos, IBlueprintType> entry : getBlueprint().getMap().entrySet()) {
            if (!entry.getValue().test(world.getBlockState(
                    new BlockPos(
                            entry.getKey().getX() + getPos().getX() - getBlueprint().getCorePos().getX(),
                            entry.getKey().getY() + getPos().getY() - getBlueprint().getCorePos().getY(),
                            entry.getKey().getZ() + getPos().getZ() - getBlueprint().getCorePos().getZ()
                    )
            ))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        String result = nbt.getString("rl");
        if (!result.equals(""))
            setBlueprint(BlueprintRegisterHandler.REGISTRY.getValue(new ResourceLocation(result)));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT result = super.write(compound);
        result.putString("rl", getBlueprint() != null ? getBlueprint().getRegistryName().toString() : "");
        return result;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getPos(), 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(world.getBlockState(pkt.getPos()), pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT result = new CompoundNBT();
        result.putString("rl", getBlueprint() != null ? getBlueprint().getRegistryName().toString() : "");
        return result;
    }
}
