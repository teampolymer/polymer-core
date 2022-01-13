package com.nmmoc7.polymercore.client.utils.world;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.math.BlockPos;

public class VirtualWorldRender {
    public void notifyBlockUpdate(VirtualWorld virtualWorld, BlockPos pos, BlockState oldState, BlockState newState, int flags) {
    }

    public void markBlockRangeForRenderUpdate(BlockPos blockPosIn, BlockState oldState, BlockState newState) {
    }

    public void markSurroundingsForRerender(int sectionX, int sectionY, int sectionZ) {
    }
}
