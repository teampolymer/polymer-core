package com.nmmoc7.polymercore.common.multiblock.builder;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.api.exceptions.MultiblockBuilderException;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.builder.ICharMarkedMultiblockBuilder;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
import com.nmmoc7.polymercore.api.multiblock.part.IPartChoice;
import com.nmmoc7.polymercore.api.multiblock.part.IPartLimitConfig;
import com.nmmoc7.polymercore.api.registry.PolymerCoreRegistries;
import com.nmmoc7.polymercore.common.multiblock.DefinedMultiblockImpl;
import com.nmmoc7.polymercore.common.multiblock.ExtensibleMultiblockImpl;
import com.nmmoc7.polymercore.common.multiblock.part.DefaultMultiblockPart;
import com.nmmoc7.polymercore.common.multiblock.part.DefaultPartChoice;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultCharMarkedMultiblockBuilder extends AbstractMultiblockBuilder<ICharMarkedMultiblockBuilder> implements ICharMarkedMultiblockBuilder {
    private Map<Character, IMultiblockPart> partsMap;
    private char[][][] pattern;
    @Nullable
    private Character coreChar;
    private final List<String[]> patternAlternative = new ArrayList<>();

    @Override
    public ICharMarkedMultiblockBuilder setParts(Map<Character, IMultiblockPart> partsMap) {
        this.partsMap = partsMap;
        return this;
    }

    @Override
    public ICharMarkedMultiblockBuilder setPatterns(char[][][] pattern) {
        this.pattern = pattern;
        return this;
    }

    @Override
    public ICharMarkedMultiblockBuilder setPatterns(String[]... pattern) {
        this.pattern = Arrays.stream(pattern)
            .map(it ->
                Arrays.stream(it)
                    .map(String::toCharArray)
                    .toArray(char[][]::new)
            ).toArray(char[][][]::new);
        return this;
    }

    @Override
    public ICharMarkedMultiblockBuilder patternLine(String... pattern) {
        patternAlternative.add(pattern);
        return this;
    }

    @Override
    public ICharMarkedMultiblockBuilder part(char ch, IMultiblockPart part) {
        if (partsMap == null) {
            partsMap = new HashMap<>();
        }
        partsMap.put(ch, part);
        return this;
    }

    @Override
    public ICharMarkedMultiblockBuilder core(char ch) {
        this.coreChar = ch;
        return this;
    }

    @Override
    public IDefinedMultiblock build() {
        if (machine == null) {
            //TODO: Machine
//            throw new MultiblockBuilderException("'Machine' can not be null");
        }
        if (type == null) {
            type = PolymerCoreRegistries.MULTIBLOCK_TYPES.getValue(new ResourceLocation(PolymerCoreApi.MOD_ID, "type_free"));
        }
        if (pattern == null) {
            setPatterns(patternAlternative.toArray(new String[0][0]));
        }
        Vector3i coreOffset = null;
        Map<Vector3i, IMultiblockPart> resolvedParts = new HashMap<>();
        int maxX = 0, maxY = 0, maxZ = 0;
        for (int y = 0; y < pattern.length; y++) {
            maxY = Math.max(y, maxY);
            for (int z = 0; z < pattern[y].length; z++) {
                char[] chars = pattern[y][z];
                maxZ = Math.max(z, maxZ);
                for (int x = 0; x < chars.length; x++) {
                    Vector3i position = new Vector3i(x, y, z);
                    char ch = chars[x];
                    if (ch == ' ') {
                        continue;
                    }
                    maxX = Math.max(x, maxX);
                    if (coreChar != null && ch == coreChar) {
                        coreOffset = position;
                    }
                    IMultiblockPart part = partsMap.get(ch);
                    if (part == null) {
                        throw new MultiblockBuilderException("Could not find MultiblockPart matching char: " + ch);
                    }
                    if (coreChar == null) {
                        if (coreOffset != null) {
                            throw new MultiblockBuilderException("There are more than one multiblock core in the structure!");
                        }
                        coreOffset = position;
                    }
                    resolvedParts.put(position, part);
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
                if (copy.equals(Vector3i.ZERO)) {
                    return raw;
                }
                return new Vector3i(
                    raw.getX() - copy.getX(),
                    raw.getY() - copy.getY(),
                    raw.getZ() - copy.getZ()
                );
            }, Map.Entry::getValue));

        Vector3i size = new Vector3i(maxX, maxY, maxZ);

        List<IPartLimitConfig> limitConfigs = buildLimitConfigs();
        //可拓展的版本
        if (extensions.size() > 0) {
            return new ExtensibleMultiblockImpl(
                components,
                machine,
                size,
                result,
                type,
                canSymmetrical,
                extensions,
                tags,
                limitConfigs
            );
        }
        //不可拓展的版本
        return new DefinedMultiblockImpl(
            components,
            machine,
            size,
            result,
            type,
            canSymmetrical,
            tags,
            limitConfigs);

    }

}
