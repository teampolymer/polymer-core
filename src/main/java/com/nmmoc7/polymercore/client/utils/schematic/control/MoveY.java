package com.nmmoc7.polymercore.client.utils.schematic.control;

import com.nmmoc7.polymercore.client.resources.Icons;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class MoveY extends MoveAbstract {

    public MoveY() {
        super(Icons.MOVE_Y);
    }

    @Override
    public ITextComponent getName() {
        return new TranslationTextComponent("gui.polymer.locator.control.move_y.title");
    }


    @Override
    public BlockPos doMove(BlockPos current, int move) {
        return current.add(0, move, 0);
    }
}
