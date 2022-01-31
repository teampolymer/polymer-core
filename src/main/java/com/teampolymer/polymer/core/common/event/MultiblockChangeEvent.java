package com.teampolymer.polymer.core.common.event;

import com.teampolymer.polymer.core.api.multiblock.IAssembledMultiblock;
import com.teampolymer.polymer.core.api.multiblock.IMultiblockType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.eventbus.api.Event;

public abstract class MultiblockChangeEvent extends Event {

    public MultiblockChangeEvent(ServerWorld world, IAssembledMultiblock multiblock, BlockPos corePos) {
        this.world = world;
        this.multiblock = multiblock;
        this.corePos = corePos;
    }
    private final ServerWorld world;
    private final IAssembledMultiblock multiblock;
    private final BlockPos corePos;

    public ServerWorld getWorld() {
        return world;
    }

    public IAssembledMultiblock getMultiblock() {
        return multiblock;
    }

    public BlockPos getCorePos() {
        return corePos;
    }

    public static class Assembled extends MultiblockChangeEvent {

        public Assembled(ServerWorld world, IAssembledMultiblock multiblock, BlockPos corePos) {
            super(world, multiblock, corePos);
        }
    }

    public static class Disassembled extends MultiblockChangeEvent {

        public Disassembled(ServerWorld world, IAssembledMultiblock multiblock, BlockPos corePos) {
            super(world, multiblock, corePos);
        }
    }


}