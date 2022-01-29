package com.teampolymer.polymer.core.common.multiblock.builder;

import com.teampolymer.polymer.core.api.component.IMultiblockComponent;
import com.teampolymer.polymer.core.api.multiblock.IMultiblockType;
import com.teampolymer.polymer.core.api.multiblock.builder.IMultiblockBuilder;
import com.teampolymer.polymer.core.api.multiblock.extension.IMultiblockExtension;
import com.teampolymer.polymer.core.api.multiblock.part.IPartLimitConfig;
import com.teampolymer.polymer.core.api.registry.PolymerCoreRegistries;
import com.teampolymer.polymer.core.common.multiblock.config.MutableLimitConfig;
import net.minecraft.util.ResourceLocation;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public abstract class AbstractMultiblockBuilder<T extends IMultiblockBuilder<T>> implements IMultiblockBuilder<T> {
    protected IMultiblockType type;
    protected String machine;
    protected boolean canSymmetrical = false;
    protected final List<IMultiblockExtension> extensions = new ArrayList<>();
    protected final List<IMultiblockComponent> components = new ArrayList<>();
    protected final List<String> tags = new ArrayList<>();

    protected final Map<String, MutableLimitConfig> limitConfigs = new HashMap<>();

    @Override
    public T addTags(String... tags) {
        this.tags.addAll(Arrays.asList(tags));
        return (T) this;
    }

    @Override
    public T limit(String partType, int min, int max) {
        MutableLimitConfig limitConfig = limitConfigs.computeIfAbsent(partType, it -> new MutableLimitConfig(partType));
        limitConfig.setMax(max);
        limitConfig.setMin(min);
        return (T) this;
    }

    @Override
    public T limitMax(String partType, int max) {
        MutableLimitConfig limitConfig = limitConfigs.computeIfAbsent(partType, it -> new MutableLimitConfig(partType));
        limitConfig.setMax(max);
        return (T) this;
    }

    @Override
    public T limitMin(String partType, int min) {
        MutableLimitConfig limitConfig = limitConfigs.computeIfAbsent(partType, it -> new MutableLimitConfig(partType));
        limitConfig.setMin(min);
        return (T) this;
    }

    @Override
    public T machine(String machine) {
        this.machine = machine;
        return (T) this;
    }

    @Override
    public T type(IMultiblockType type) {
        this.type = type;
        return (T) this;
    }

    @Override
    public T type(String type) {
        IMultiblockType value = PolymerCoreRegistries.MULTIBLOCK_TYPES.getValue(new ResourceLocation(type));
        type(value);
        return (T) this;
    }

    @Override
    public T addComponent(IMultiblockComponent component) {
        components.add(component);
        return (T) this;
    }

    @Override
    public T addComponents(IMultiblockComponent... component) {
        components.addAll(Arrays.asList(component));
        return (T) this;
    }

    @Override
    public T addExtension(IMultiblockExtension extension) {
        extensions.add(extension);
        return (T) this;
    }

    @Override
    public T allowSymmetrical(boolean canSymmetrical) {
        this.canSymmetrical = canSymmetrical;
        return (T) this;
    }

    protected List<IPartLimitConfig> buildLimitConfigs() {
        return this.limitConfigs.values().stream().map(MutableLimitConfig::toImmutable).collect(Collectors.toList());
    }
}
