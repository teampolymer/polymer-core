package com.nmmoc7.polymercore.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;

/**
 * 游戏渲染时间处理，参考Create
 */
public final class AnimationTickHelper {

    private static int ticks = 0;
    private static int pausedTicks = 0;


    public static void reset() {
        ticks = 0;
        pausedTicks = 0;
    }

    public static void tick() {
        if (!Minecraft.getInstance().isPaused()) {
            ticks = (ticks + 1) % 1_728_000; // wrap around every 24 hours so we maintain enough floating point precision
        } else {
            pausedTicks = (pausedTicks + 1) % 1_728_000;
        }
    }

    public static int getTicks() {
        return getTicks(false);
    }

    public static int getTicks(boolean includePaused) {
        return includePaused ? ticks + pausedTicks : ticks;
    }

    public static float getRenderTime() {
        return getTicks() + getPartialTicks();
    }

    public static float getPartialTicks() {
        Minecraft mc = Minecraft.getInstance();
        return (mc.isPaused() ? mc.pausePartialTick : mc.getFrameTime());
    }

    /**
     * 在一个周期内循环一串数字
     *
     * @param begin  开始
     * @param end    结束
     * @param period 周期
     * @return 循环的数字
     */
    public static float circulateIn(float begin, float end, float period) {
        return begin + (end - begin) * (getRenderTime() % period / period);
    }

    public static float circulateIn(float end, float period) {
        return end * (getRenderTime() % period / period);
    }

    public static float sinCirculateIn(float begin, float end, float period) {
        return begin + (MathHelper.sin(getRenderTime() * 2 * 3.14159265358979323846f / period) + 1f) / 2 * (end - begin);
    }

    public static float sinCirculateIn(float end, float period) {
        return (MathHelper.sin(getRenderTime() * 2 * 3.14159265358979323846f / period) + 1f) / 2 * end;
    }

}
