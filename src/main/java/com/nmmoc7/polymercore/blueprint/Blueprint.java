package com.nmmoc7.polymercore.blueprint;

import com.nmmoc7.polymercore.blueprint.type.IBlueprintType;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Blueprint implements IBlueprint {
    public final Map<BlockPos, IBlueprintType> map = new HashMap<>();

    public Blueprint(String[][][] in, Def... types) {
        for (int i = 0; i < in.length; i++) {
            for (int i1 = 0; i1 < in[i].length; i1++) {
                for (int i2 = 0; i2 < in[i][i1].length; i2++) {
                    char[] strArr = in[i][i1][i2].toCharArray();

                    for (char c : strArr) {
                        for (Def def : types) {
                            if (def.key == c) {
                                map.put(new BlockPos(i1, i, i2), def.type);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public Map<BlockPos, IBlueprintType> getMap() {
        return map;
    }

    public static class Def {
        char key;
        IBlueprintType type;

        public Def(char key, IBlueprintType type) {
            this.key = key;
            this.type = type;
        }
    }
}
