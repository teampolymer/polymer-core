package com.nmmoc7.polymercore.common.multiblock.builder;

import com.nmmoc7.polymercore.api.multiblock.builder.IPartBuilder;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
import com.nmmoc7.polymercore.api.multiblock.part.IPartChoice;
import com.nmmoc7.polymercore.common.multiblock.part.DefaultMultiblockPart;

import java.util.ArrayList;
import java.util.List;

public class DefaultPartBuilder implements IPartBuilder {
    public List<IPartChoice> choiceList = new ArrayList<>();

    @Override
    public IPartBuilder setDefaultPart(IMultiblockUnit unit) {
        choiceList.add(new DefaultMultiblockPart.DefaultPartChoice(null, unit));
        return this;
    }

    @Override
    public IPartBuilder addChoice(String type, IMultiblockUnit unit) {
        choiceList.add(new DefaultMultiblockPart.DefaultPartChoice(type, unit));
        return this;
    }

    @Override
    public IPartBuilder addChoice(String type, IMultiblockUnit unit, boolean canBeSample) {
        choiceList.add(new DefaultMultiblockPart.DefaultPartChoice(type, unit, canBeSample));
        return this;
    }

    @Override
    public IMultiblockPart build() {
        return new DefaultMultiblockPart(choiceList);
    }
}
