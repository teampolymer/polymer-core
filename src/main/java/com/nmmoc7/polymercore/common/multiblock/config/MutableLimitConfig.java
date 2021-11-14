package com.nmmoc7.polymercore.common.multiblock.config;

import com.nmmoc7.polymercore.api.multiblock.part.IPartLimitConfig;

public class MutableLimitConfig implements IPartLimitConfig {
    private String target;
    private int max;

    private int min;

    public MutableLimitConfig(String target, int max, int min) {
        this.target = target;
        this.max = max;
        this.min = min;
    }

    public MutableLimitConfig(String target) {
        this.target = target;
        this.max = -1;
        this.min = -1;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public IPartLimitConfig toImmutable() {
        return new SimpleLimitConfig(target, max, min);
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
