package com.nmmoc7.polymercore.common.multiblock.part;

import com.nmmoc7.polymercore.api.exceptions.MultiblockBuilderException;
import com.nmmoc7.polymercore.api.multiblock.MultiblockDirection;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
import com.nmmoc7.polymercore.api.multiblock.part.IPartChoice;
import net.minecraft.block.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class DefaultMultiblockPart implements IMultiblockPart {
    private final List<IPartChoice> sampleChoices;
    private final Map<String, IPartChoice> choiceMap;

    public DefaultMultiblockPart(List<IPartChoice> entries) {
        this.sampleChoices = new ArrayList<>();
        this.choiceMap = new HashMap<>();
        for (IPartChoice entry : entries) {
            String type = entry.getType();
            if (this.choiceMap.put(type, entry) != null) {
                throw new MultiblockBuilderException("Part choices has duplicate types");
            }
            if (entry.canBeSample() || type == null) {
                sampleChoices.add(entry);
            }
        }

    }

    @Override
    public IPartChoice pickupChoice(BlockState possible, MultiblockDirection direction) {

        for (IPartChoice choice : choiceMap.values()) {
            if (choice.getUnit(direction).test(possible)) {
                return choice;
            }
        }
        return null;
    }

    @Override
    public @Nullable IPartChoice pickupChoice(@Nullable String type) {
        return choiceMap.get(type);
    }

    @Override
    public @Nullable IPartChoice defaultChoice() {
        return choiceMap.get(null);
    }

    @Override
    public Collection<IPartChoice> choices() {
        return choiceMap.values();
    }

    @Override
    public Collection<IPartChoice> sampleChoices() {
        return sampleChoices;
    }

}
