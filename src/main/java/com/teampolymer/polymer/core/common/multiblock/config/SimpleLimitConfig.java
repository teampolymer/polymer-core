package com.teampolymer.polymer.core.common.multiblock.config;

import com.teampolymer.polymer.core.api.multiblock.part.IPartLimitConfig;

public class SimpleLimitConfig implements IPartLimitConfig {
    private final String target;
    private final int max;
    private final int min;

    public SimpleLimitConfig(String target, int max, int min) {
        this.target = target;
        this.max = max;
        this.min = min;
    }



    @Override
    public String getTargetType() {
        return target;
    }

    @Override
    public int maxCount() {
        return max;
    }

    @Override
    public int minCount() {
        return min;
    }
}
