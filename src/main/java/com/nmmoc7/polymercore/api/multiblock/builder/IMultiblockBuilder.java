package com.nmmoc7.polymercore.api.multiblock.builder;

import com.nmmoc7.polymercore.api.component.IMultiblockComponent;
import com.nmmoc7.polymercore.api.machine.IMachine;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.IMultiblockType;
import com.nmmoc7.polymercore.api.multiblock.extension.IMultiblockExtension;

public interface IMultiblockBuilder {
    IDefinedMultiblock build();

    IMultiblockBuilder setMachine(IMachine machine);

    IMultiblockBuilder setType(IMultiblockType type);

    IMultiblockBuilder addComponent(IMultiblockComponent component);
    IMultiblockBuilder addComponents(IMultiblockComponent ...component);

    IMultiblockBuilder addExtension(IMultiblockExtension extension);

    default IMultiblockBuilder setCanSymmetrical() {
        return setCanSymmetrical(true);
    }

    IMultiblockBuilder setCanSymmetrical(boolean canSymmetrical);

}
