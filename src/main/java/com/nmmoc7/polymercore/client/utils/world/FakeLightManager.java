package com.nmmoc7.polymercore.client.utils.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.SectionPos;
import net.minecraft.world.LightType;
import net.minecraft.world.chunk.IChunkLightProvider;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.lighting.IWorldLightListener;
import net.minecraft.world.lighting.WorldLightManager;
import org.jetbrains.annotations.Nullable;

public class FakeLightManager extends WorldLightManager {
    private final FakeLightingView lightingView;

    public FakeLightManager() {
        super(null, false, false);

        this.lightingView = new FakeLightingView();
    }


    @Override
    public IWorldLightListener getLayerListener(LightType type) {
        return this.lightingView;
    }


    @Override
    public int getRawBrightness(BlockPos blockPosIn, int amount) {
        return 15;
    }

    public static class FakeLightingView implements IWorldLightListener {
        @Nullable
        @Override
        public NibbleArray getDataLayerData(SectionPos p_215612_1_) {
            return null;
        }

        @Override
        public int getLightValue(BlockPos worldPos) {
            return 15;
        }

        @Override
        public void updateSectionStatus(SectionPos pos, boolean isEmpty) {

        }
    }
}

