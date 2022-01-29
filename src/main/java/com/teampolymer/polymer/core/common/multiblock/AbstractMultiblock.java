package com.teampolymer.polymer.core.common.multiblock;

import com.teampolymer.polymer.core.api.component.IMultiblockComponent;
import com.teampolymer.polymer.core.api.multiblock.IMultiblock;
import com.teampolymer.polymer.core.api.util.AttributeKey;
import com.teampolymer.polymer.core.api.util.AttributeMap;
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
