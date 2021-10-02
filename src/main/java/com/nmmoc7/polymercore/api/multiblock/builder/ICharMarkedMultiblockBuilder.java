package com.nmmoc7.polymercore.api.multiblock.builder;


import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;

import java.util.Map;

/**
 * 用字符数组定义的多方快结构
 */
public interface ICharMarkedMultiblockBuilder extends IMultiblockBuilder {
    ICharMarkedMultiblockBuilder setPartsMap(Map<Character, IMultiblockPart> partsMap);
    ICharMarkedMultiblockBuilder setPattern(char[][][] pattern);
}
