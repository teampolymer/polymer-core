package com.nmmoc7.polymercore.common.multiblock.builder;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.api.component.IMultiblockComponent;
import com.nmmoc7.polymercore.api.exceptions.MultiblockBuilderException;
import com.nmmoc7.polymercore.api.machine.IMachine;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.IMultiblockType;
import com.nmmoc7.polymercore.api.multiblock.builder.ICharMarkedMultiblockBuilder;
import com.nmmoc7.polymercore.api.multiblock.builder.IMultiblockBuilder;
import com.nmmoc7.polymercore.api.multiblock.extension.IMultiblockExtension;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
import com.nmmoc7.polymercore.api.multiblock.part.IPartChoice;
import com.nmmoc7.polymercore.api.multiblock.part.IPartLimitConfig;
import com.nmmoc7.polymercore.api.registry.PolymerCoreRegistries;
import com.nmmoc7.polymercore.common.multiblock.DefinedMultiblockImpl;
import com.nmmoc7.polymercore.common.multiblock.ExtensibleMultiblockImpl;
import com.nmmoc7.polymercore.common.multiblock.config.MutableLimitConfig;
import com.nmmoc7.polymercore.common.multiblock.part.DefaultMultiblockPart;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;

import javax.annotation.Nullable;
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
