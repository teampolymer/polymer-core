package com.nmmoc7.polymercore.core.event;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event;

/**
 * 方块更新事件
 */
public class BlockUpdateEvent extends Event {
    private final World world;
    private final BlockPos pos;
    private final Block oldBlock;

    public BlockUpdateEvent(World world, BlockPos pos, Block oldBlock) {
        this.world = world;
        this.pos = pos;
        this.oldBlock = oldBlock;
    }


    public BlockPos getPos() {
        return pos;
    }

    public World getWorld() {
        return world;
    }


    public Block getOldBlock() {
        return oldBlock;
    }

}
