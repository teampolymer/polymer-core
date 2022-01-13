package com.nmmoc7.polymercore.client.utils.world;

import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.DefaultBiomeMagnifier;
import net.minecraft.world.biome.IBiomeMagnifier;

import java.util.OptionalLong;

public final class MyDimensionType extends DimensionType
{
    private MyDimensionType(OptionalLong fixedTime,
                            boolean hasSkylight,
                            boolean hasCeiling,
                            boolean ultrawarm,
                            boolean natural,
                            double coordinateScale,
                            boolean hasEnderDragonFight,
                            boolean piglinSafe,
                            boolean bedWorks,
                            boolean respawnAnchorWorks,
                            boolean hasRaids,
                            int logicalHeight,
                            IBiomeMagnifier biomeAccessType,
                            ResourceLocation infiniburn,
                            ResourceLocation skyProperties,
                            float ambientLight)
    {
        super(fixedTime, hasSkylight, hasCeiling, ultrawarm, natural, coordinateScale,
            hasEnderDragonFight, piglinSafe, bedWorks, respawnAnchorWorks, hasRaids,
            logicalHeight, biomeAccessType, infiniburn, skyProperties, ambientLight);
    }

    public static final DimensionType VIRTUAL_EMPTY =
        new MyDimensionType(OptionalLong.of(6000L), false, false, false, false,
            1.0D, false, false, false, false, true, 256,
            DefaultBiomeMagnifier.INSTANCE,
            BlockTags.INFINIBURN_END.getName(),
            DimensionType.END_EFFECTS, 0.0F);
}