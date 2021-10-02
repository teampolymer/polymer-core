package com.nmmoc7.polymercore.common.multiblock.builder;

import com.nmmoc7.polymercore.api.component.IMultiblockComponent;
import com.nmmoc7.polymercore.api.exceptions.MultiblockBuilderException;
import com.nmmoc7.polymercore.api.machine.IMachine;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.IMultiblockType;
import com.nmmoc7.polymercore.api.multiblock.builder.ICharMarkedMultiblockBuilder;
import com.nmmoc7.polymercore.api.multiblock.builder.IMultiblockBuilder;
import com.nmmoc7.polymercore.api.multiblock.extension.IMultiblockExtension;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockCore;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.common.multiblock.DefinedMultiblockImpl;
import com.nmmoc7.polymercore.common.multiblock.ExtensibleMultiblockImpl;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.vector.Vector3i;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultCharMarkedMultiblockBuilder implements ICharMarkedMultiblockBuilder {
    private Map<Character, IMultiblockPart> partsMap;
    private char[][][] pattern;
    private IMultiblockType type;
    private IMachine machine;
    private boolean canSymmetrical;
    private final List<IMultiblockExtension> extensions = new ArrayList<>();
    private final List<IMultiblockComponent> components = new ArrayList<>();

    @Override
    public ICharMarkedMultiblockBuilder setPartsMap(Map<Character, IMultiblockPart> partsMap) {
        this.partsMap = partsMap;
        return this;
    }

    @Override
    public ICharMarkedMultiblockBuilder setPattern(char[][][] pattern) {
        this.pattern = pattern;
        return this;
    }

    public ICharMarkedMultiblockBuilder setPattern(String[]... pattern) {
        this.pattern = Arrays.stream(pattern)
            .map(it ->
                Arrays.stream(it)
                    .map(String::toCharArray)
                    .toArray(char[][]::new)
            ).toArray(char[][][]::new);
        return this;
    }

    @Override
    public IDefinedMultiblock build() {
        Vector3i coreOffset = null;
        Map<Vector3i, IMultiblockPart> resolvedParts = new HashMap<>();
        int maxX = 0, maxY = 0, maxZ = 0;
        for (int y = 0; y < pattern.length; y++) {
            maxY = Math.max(y, maxY);
            for (int x = 0; x < pattern[y].length; x++) {
                char[] chars = pattern[y][x];
                maxX = Math.max(x, maxX);
                for (int z = 0; z < chars.length; z++) {
                    Vector3i postion = new Vector3i(x, y, z);
                    char ch = chars[z];
                    if (ch == ' ') {
                        continue;
                    }
                    maxZ = Math.max(z, maxZ);
                    IMultiblockPart part = partsMap.get(ch);
                    if (part == null) {
                        throw new MultiblockBuilderException("Could not find MultiblockPart matching char: " + ch);
                    }
                    if (part instanceof IMultiblockCore) {
                        if (coreOffset != null) {
                            throw new MultiblockBuilderException("There are more than one multiblock core in the structure!");
                        }
                        coreOffset = postion;
                    }
                    resolvedParts.put(postion, part);
                }
            }
        }
        if (coreOffset == null) {
            throw new MultiblockBuilderException("Could not find a multiblock core in the structure!");
        }
        Vector3i copy = coreOffset;
        Map<Vector3i, IMultiblockPart> result = resolvedParts.entrySet().stream()
            .collect(Collectors.toMap(it -> {
                    Vector3i raw = it.getKey();
                    if (copy.equals(Vector3i.NULL_VECTOR)) {
                        return raw;
                    }
                    return new Vector3i(
                        raw.getX() - copy.getX(),
                        raw.getY() - copy.getY(),
                        raw.getZ() - copy.getZ()
                    );
                }
                , Map.Entry::getValue));

        Vector3i size = new Vector3i(maxX, maxY, maxZ);
        //可拓展的版本
        if (extensions.size() > 0) {
            return new ExtensibleMultiblockImpl(
                components,
                machine,
                size,
                result,
                type,
                canSymmetrical,
                extensions
            );
        }
        //不可拓展的版本
        return new DefinedMultiblockImpl(
            components,
            machine,
            size,
            result,
            type,
            canSymmetrical
        );

    }

    @Override
    public IMultiblockBuilder setMachine(IMachine machine) {
        this.machine = machine;
        return this;
    }

    @Override
    public IMultiblockBuilder setType(IMultiblockType type) {
        this.type = type;
        return this;
    }

    @Override
    public IMultiblockBuilder addComponent(IMultiblockComponent component) {
        components.add(component);
        return this;
    }

    @Override
    public IMultiblockBuilder addComponents(IMultiblockComponent... component) {
        components.addAll(Arrays.asList(component));
        return this;
    }

    @Override
    public IMultiblockBuilder addExtension(IMultiblockExtension extension) {
        extensions.add(extension);
        return this;
    }

    @Override
    public IMultiblockBuilder setCanSymmetrical(boolean canSymmetrical) {
        this.canSymmetrical = canSymmetrical;
        return this;
    }
}
