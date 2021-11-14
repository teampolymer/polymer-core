package com.nmmoc7.polymercore.common.multiblock;

import com.nmmoc7.polymercore.api.component.IMultiblockComponent;
import com.nmmoc7.polymercore.api.machine.IMachine;
import com.nmmoc7.polymercore.api.multiblock.IMultiblock;
import com.nmmoc7.polymercore.api.util.AttributeKey;
import com.nmmoc7.polymercore.api.util.AttributeMap;
import net.minecraft.util.math.vector.Vector3i;

import java.util.List;

public abstract class AbstractMultiblock implements IMultiblock {
    private final List<IMultiblockComponent> components;
    private final String machine;
    private final Vector3i size;
    private final AttributeMap attributeMap = new AttributeMap();

    public AbstractMultiblock(List<IMultiblockComponent> components, String machine, Vector3i size) {
        this.components = components;
        this.machine = machine;
        this.size = size;
    }


    @Override
    public List<IMultiblockComponent> getComponents() {
        return components;
    }

    @Override
    public String getMachine() {
        return machine;
    }

    @Override
    public Vector3i getSize() {
        return size;
    }

    @Override
    public <T> void setAttribute(AttributeKey<T> key, T value) {
        attributeMap.setAttribute(key, value);
    }

    @Override
    public <T> T getAttribute(AttributeKey<T> key) {
        return attributeMap.getAttribute(key);
    }
}
