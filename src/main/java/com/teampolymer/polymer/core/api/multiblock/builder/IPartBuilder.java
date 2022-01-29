package com.teampolymer.polymer.core.api.multiblock.builder;

import com.teampolymer.polymer.core.api.multiblock.part.IMultiblockPart;
import com.teampolymer.polymer.core.api.multiblock.part.IMultiblockUnit;

public interface IPartBuilder {
    IPartBuilder defaultPart(IMultiblockUnit unit);
    IPartBuilder choice(String type, IMultiblockUnit unit);
    IPartBuilder choice(String type, IMultiblockUnit unit, boolean canBeSample);
    IMultiblockPart build();
}
