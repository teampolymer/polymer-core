package com.nmmoc7.polymercore.common.blueprint;

import com.nmmoc7.polymercore.api.blueprint.IBlueprint;
import com.nmmoc7.polymercore.api.blueprint.type.IBlueprintType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.HashMap;
import java.util.Map;

public class Blueprint extends ForgeRegistryEntry<IBlueprint> implements IBlueprint {
    public BlockPos corePos;
    public IBlueprintType core;
    public final Map<BlockPos, IBlueprintType> map = new HashMap<>();

    public Blueprint(String[][] in, Def core, Def... types) {
        for (int i = 0; i < in.length; i++) {
            for (int i1 = 0; i1 < in[i].length; i1++) {
                char[] strArr = in[i][i1].toCharArray();

                for (int i2 = 0; i2 < strArr.length; i2++) {
                    for (Def def : types) {
                        if (def.key == strArr[i2]) {
                            map.put(new BlockPos(i1, i, i2), def.type);
                        }
                        else if (core.key == strArr[i2]) {
                            corePos = new BlockPos(i1, i, i2);
                            this.core = core.type;
                        }
                    }
                }
            }
        }

        if (core == null) {
            String throwInfo = new TranslationTextComponent("error.blueprint.no_core").getString();
            throw new IllegalArgumentException(throwInfo);
        }
    }

    @Override
    public Map<BlockPos, IBlueprintType> getMap() {
        return map;
    }

    @Override
    public BlockPos getCorePos() {
        return corePos;
    }

    @Override
    public IBlueprintType getCoreType() {
        return core;
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
