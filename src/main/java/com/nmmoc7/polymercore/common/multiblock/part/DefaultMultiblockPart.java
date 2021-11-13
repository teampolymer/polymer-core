package com.nmmoc7.polymercore.common.multiblock.part;

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
            this.choiceMap.put(entry.getType(), entry);
            if (entry.canBeSample()) {
                sampleChoices.add(entry);
            }
        }
    }

    @Override
    public IPartChoice pickupChoice(BlockState possible) {
        for (IPartChoice choice : choiceMap.values()) {
            if (choice.getUnit().test(possible)) {
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

    public static class DefaultPartChoice implements IPartChoice {
        private final String type;
        private final IMultiblockUnit unit;
        private final boolean sample;

        public DefaultPartChoice(String type, IMultiblockUnit unit) {
            this.type = type;
            this.unit = unit;
            this.sample = type != null;
        }

        public DefaultPartChoice(String type, IMultiblockUnit unit, boolean canBeSample) {
            this.type = type;
            this.unit = unit;
            this.sample = canBeSample;
        }

        @Override
        public IMultiblockUnit getUnit() {
            return unit;
        }

        @Override
        public String getType() {
            return type;
        }

        @Override
        public boolean canBeSample() {
            return sample;
        }
    }
}
