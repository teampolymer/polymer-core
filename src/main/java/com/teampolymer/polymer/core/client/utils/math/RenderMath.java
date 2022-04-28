package com.teampolymer.polymer.core.client.utils.math;

public class RenderMath {
    /**
     * Decompose a 32-bit color into ARGB 8-bit int values
     * @param color color to decompose
     * @return 4-element array of ints
     */
    public static int[] decomposeColor(int color) {
        int[] res = new int[4];
        res[0] = color >> 24 & 0xff;
        res[1] = color >> 16 & 0xff;
        res[2] = color >> 8  & 0xff;
        res[3] = color       & 0xff;
        return res;
    }

    public static float[] decomposeColorF(int color) {
        float[] res = new float[4];
        res[0] = (color >> 24 & 0xff) / 255f;
        res[1] = (color >> 16 & 0xff) / 255f;
        res[2] = (color >> 8  & 0xff) / 255f;
        res[3] = (color       & 0xff) / 255f;
        return res;
    }
}
