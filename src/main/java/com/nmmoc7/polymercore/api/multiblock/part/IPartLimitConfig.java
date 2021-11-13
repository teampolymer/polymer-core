package com.nmmoc7.polymercore.api.multiblock.part;

import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * 设定多方快结构中部分可变组件的数量限制
 */
public interface IPartLimitConfig {

    /**
     * 获取应用统计的组件类型
     * @return 组件类型
     */
    String getTargetType();

    /**
     * 最大的数量，-1则不限制
     */
    int maxCount();
    /**
     * 最少的数量，-1则不限制
     */
    int minCount();

}
