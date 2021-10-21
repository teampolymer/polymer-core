package com.nmmoc7.polymercore.client.utils.animate;

import com.nmmoc7.polymercore.client.utils.AngleHelper;
import net.minecraft.util.math.MathHelper;

//Modify form com.simibubi.create.foundation.gui.widgets.InterpolatedValue
public class AnimatedValue {
    protected AnimatedValue() {

    }

    public static AnimatedValue angle() {
        return new Angle();
    }

    public static AnimatedValue normal() {
        return new AnimatedValue();
    }

    public float value = 0;
    public float lastValue = 0;
    private boolean settled = true;
    float speed = 0.5f;
    float target = 0;
    float eps = 1 / 4096f;

    public AnimatedValue set(float value) {
        lastValue = this.value;
        this.value = value;
        return this;
    }

    public AnimatedValue init(float value) {
        this.lastValue = this.value = value;
        return this;
    }

    public float get(float partialTicks) {
        return MathHelper.lerp(partialTicks, lastValue, value);
    }

    public boolean settled() {
        return settled;
    }


    public void tick() {
        float diff = getCurrentDiff();
        if (Math.abs(diff) < eps) {
            settled = true;
            return;
        }
        settled = false;
        set(value + (diff) * speed);
    }

    protected float getCurrentDiff() {
        return getTarget() - value;
    }

    public AnimatedValue withSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    public AnimatedValue target(float target) {
        this.target = target;
        return this;
    }

    public AnimatedValue start(float value) {
        lastValue = this.value = value;
        target(value);
        return this;
    }

    public float getTarget() {
        return target;
    }

    private static class Angle extends AnimatedValue {

        public float get(float partialTicks) {
            return AngleHelper.angleLerp(partialTicks, lastValue, value);
        }

        @Override
        protected float getCurrentDiff() {
            return AngleHelper.getShortestAngleDiff(value, getTarget());
        }

    }


}
