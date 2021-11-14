package com.nmmoc7.polymercore.common.registry;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.common.event.MultiblockReloadListener;
import com.nmmoc7.polymercore.common.handler.MultiblockRegisterHandler;
import com.nmmoc7.polymercore.common.multiblock.builder.DefaultCharMarkedMultiblockBuilder;
import com.nmmoc7.polymercore.common.multiblock.unit.UnitSpecifiedBlock;
import net.minecraft.block.Blocks;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PolymerCoreApi.MOD_ID)
public class ReloadListenerHandler {
    @SubscribeEvent
    public static void onAddReloadListener(AddReloadListenerEvent e) {
        e.addListener(new MultiblockReloadListener());
    }

    public static class DefaultListener extends ReloadListener<Object> {

        @Override
        protected Object prepare(IResourceManager resourceManagerIn, IProfiler profilerIn) {

            return null;
        }

        @Override
        protected void apply(Object objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
//            IDefinedMultiblock build = new DefaultCharMarkedMultiblockBuilder()
//                .patternLine(
//                    "AAAAA",
//                    "AABAAA",
//                    "AAAAA",
//                    " B"
//                )
//                .patternLine(
//                    "AAAAA",
//                    "AWBWA",
//                    "AAAFA"
//                )
//                .patternLine(
//                    "     ",
//                    "  B  ",
//                    "     "
//                ).patternLine(
//                    "",
//                    "  C"
//                )
//                .part('A', new UnitSpecifiedBlock(Blocks.STONE))
//                .part('B', new UnitSpecifiedBlock(Blocks.OAK_WOOD))
//                .part('C', new UnitSpecifiedBlock(Blocks.GLOWSTONE))
//                .part('W', new UnitSpecifiedBlock(Blocks.WATER))
//                .part('F', new UnitSpecifiedBlock(Blocks.CHEST))
//                .core('C')
//                .type(MultiblockRegisterHandler.TYPE_FREE.get())
//                .allowSymmetrical()
//                .build();
//
//            PolymerCoreApi.getInstance().getMultiblockManager().addDefinedMultiblock(build.setRegistryName(new ResourceLocation(PolymerCoreApi.MOD_ID, "test_machine")));
//
//
//            IDefinedMultiblock build2 = new DefaultCharMarkedMultiblockBuilder()
//                .patternLine(
//                    "ACCCA",
//                    "AAAAA",
//                    "AABAD"
//                )
//                .patternLine(
//                    "AAEEA",
//                    "A B A",
//                    "AEAAA"
//                )
//                .patternLine(
//                    "     ",
//                    "  B  ",
//                    "     "
//                )
//                .part('A', new UnitSpecifiedBlock(Blocks.GOLD_BLOCK))
//                .part('B', new UnitSpecifiedBlock(Blocks.OAK_WOOD))
//                .part('C', new UnitSpecifiedBlock(Blocks.DIAMOND_BLOCK))
//                .part('D', new UnitSpecifiedBlock(Blocks.GLOWSTONE))
//                .part('E', new UnitSpecifiedBlock(Blocks.QUARTZ_STAIRS))
//
//                .core('D')
//                .type(MultiblockRegisterHandler.TYPE_FREE.get())
//                .allowSymmetrical()
//                .build();
//
//            PolymerCoreApi.getInstance().getMultiblockManager().addDefinedMultiblock(build2.setRegistryName(new ResourceLocation(PolymerCoreApi.MOD_ID, "test_machine_2")));


        }
    }

}
