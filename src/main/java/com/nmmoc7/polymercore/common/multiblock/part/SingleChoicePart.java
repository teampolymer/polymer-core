package com.nmmoc7.polymercore.common.multiblock.part;

import com.nmmoc7.polymercore.api.multiblock.MultiblockDirection;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.multiblock.part.IPartChoice;
import net.minecraft.block.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class SingleChoicePart implements IMultiblockPart {
    private final IPartChoice choice;

    public SingleChoicePart(IPartChoice choice) {
        this.choice = choice;
    }

    @Override
    public @Nullable IPartChoice pickupChoice(BlockState possible, MultiblockDirection direction) {
        return choice.getUnit(direction).test(possible) ? choice : null;
    }

    @Override
    public @Nullable IPartChoice pickupChoice(@Nullable String type) {
        return Objects.equals(choice.getType(), type) ? choice : null;
    }

    @Override
    public @Nullable IPartChoice defaultChoice() {
        return choice;
    }

    @Override
    public Collection<IPartChoice> choices() {
        return Collections.singleton(choice);
    }

    @Override
    public Collection<IPartChoice> sampleChoices() {
        return Collections.singleton(choice);
    }
}
