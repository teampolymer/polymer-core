package com.nmmoc7.polymercore.api.multiblock.builder;

import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;

public interface IPartBuilder {
    IPartBuilder defaultPart(IMultiblockUnit unit);
    IPartBuilder choice(String type, IMultiblockUnit unit);
    IPartBuilder choice(String type, IMultiblockUnit unit, boolean canBeSample);
    IMultiblockPart build();
}
