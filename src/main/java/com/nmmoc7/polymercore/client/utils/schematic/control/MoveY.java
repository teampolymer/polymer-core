package com.nmmoc7.polymercore.client.utils.schematic.control;

import com.google.common.collect.Lists;
import com.nmmoc7.polymercore.client.resources.Icons;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collections;
import java.util.List;

public class MoveY extends MoveAbstract {

    public MoveY() {
        super(Icons.MOVE_Y);
    }

    @Override
    public ITextComponent getName() {
        return new TranslationTextComponent("gui.polymer.locator.control.move_y.title");
    }

    private final List<ITextComponent> description = Lists.newArrayList(
        new TranslationTextComponent("gui.polymer.locator.control.move_y.description_1"),
        new TranslationTextComponent("gui.polymer.locator.control.move_common.description_2"),
        new TranslationTextComponent("gui.polymer.locator.control.move_common.description_3")
    );

    @Override
    public List<ITextComponent> getDescription() {
        return Collections.unmodifiableList(description);
    }

    @Override
    public BlockPos doMove(BlockPos current, int move) {
        return current.offset(0, move, 0);
    }
}
