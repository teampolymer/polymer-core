package com.nmmoc7.polymercore.client.utils.world;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.DefaultBiomeMagnifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.OptionalLong;

public class VirtualWorldManager {
    private static final Logger LOG = LogManager.getLogger();

    @Nullable
    private static VirtualWorld world;


    @Nullable
    public static VirtualWorld getVirtualWorld() {
        return world;
    }

    public static VirtualWorld createVirtualWorld() {
        ClientWorld.ClientWorldInfo levelInfo = new ClientWorld.ClientWorldInfo(Difficulty.PEACEFUL, false, true);
        return new VirtualWorld(levelInfo, MyDimensionType.VIRTUAL_EMPTY, Minecraft.getInstance()::getProfiler);
    }

    public static void recreateVirtualWorld(boolean remove) {
        if (remove) {
            LOG.debug("Removing the virtual world...");

            world = null;
        } else {
            LOG.debug("Creating the virtual world...");

            // Note: The dimension used here must have no skylight, because the custom Chunks don't have those arrays
            world = createVirtualWorld();

            LOG.debug("Virtual world created: {}", world);
        }
    }
}