package com.teampolymer.polymer.core.api.multiblock.builder;


import com.teampolymer.polymer.core.api.multiblock.part.IMultiblockPart;

import java.util.Map;

/**
 * 用字符数组定义的多方快结构
 */
public interface ICharMarkedMultiblockBuilder extends IMultiblockBuilder<ICharMarkedMultiblockBuilder> {
    ICharMarkedMultiblockBuilder setParts(Map<Character, IMultiblockPart> partsMap);

    ICharMarkedMultiblockBuilder setPatterns(char[][][] pattern);

    ICharMarkedMultiblockBuilder setPatterns(String[]... pattern);

    ICharMarkedMultiblockBuilder patternLine(String... pattern);

    ICharMarkedMultiblockBuilder part(char ch, IMultiblockPart part);

    ICharMarkedMultiblockBuilder core(char ch);
}
