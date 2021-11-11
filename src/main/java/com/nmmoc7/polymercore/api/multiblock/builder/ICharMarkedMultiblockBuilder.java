package com.nmmoc7.polymercore.api.multiblock.builder;


import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;

import java.util.Map;

/**
 * 用字符数组定义的多方快结构
 */
public interface ICharMarkedMultiblockBuilder extends IMultiblockBuilder {
    ICharMarkedMultiblockBuilder setPartsMap(Map<Character, IMultiblockUnit> partsMap);
    ICharMarkedMultiblockBuilder setPattern(char[][][] pattern);
    ICharMarkedMultiblockBuilder addPattern(String... pattern);
    ICharMarkedMultiblockBuilder addPartsMap(char ch, IMultiblockUnit part);
    ICharMarkedMultiblockBuilder setCoreChar(char ch);
}
