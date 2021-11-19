package com.nmmoc7.polymercore.client.utils.schematic.control;

import com.nmmoc7.polymercore.client.resources.Icons;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class MoveX extends MoveAbstract {

    public MoveX() {
        super(Icons.MOVE_X);
    }

    @Override
    public ITextComponent getName() {
        return new TranslationTextComponent("gui.polymer.locator.control.move_x.title");
    }


    @Override
    public BlockPos doMove(BlockPos current, int move) {
        return current.add(move, 0, 0);
    }
}
