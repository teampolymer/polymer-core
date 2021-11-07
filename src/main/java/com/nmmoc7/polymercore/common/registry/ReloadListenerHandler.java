package com.nmmoc7.polymercore.common.registry;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.common.handler.MultiblockRegisterHandler;
import com.nmmoc7.polymercore.common.multiblock.builder.DefaultCharMarkedMultiblockBuilder;
import com.nmmoc7.polymercore.common.multiblock.part.PartSpecifiedBlock;
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
        e.addListener(new DefaultListener());
    }

    public static class DefaultListener extends ReloadListener<Object> {

        @Override
        protected Object prepare(IResourceManager resourceManagerIn, IProfiler profilerIn) {
            return null;
        }

        @Override
        protected void apply(Object objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
            IDefinedMultiblock build = new DefaultCharMarkedMultiblockBuilder()
                .addPattern(
                    "AAAAA",
                    "AABAAA",
                    "AAAAA",
                    " B"
                )
                .addPattern(
                    "AAAAA",
                    "A B A",
                    "AAAAA"
                )
                .addPattern(
                    "     ",
                    "  B  ",
                    "     "
                ).addPattern(
                    "",
                    "  C"
                )
                .addPartsMap('A', new PartSpecifiedBlock(Blocks.STONE))
                .addPartsMap('B', new PartSpecifiedBlock(Blocks.OAK_WOOD))
                .addPartsMap('C', new PartSpecifiedBlock(Blocks.GLOWSTONE))
                .setCoreChar('C')
                .setType(MultiblockRegisterHandler.TYPE_FREE.get())
                .setCanSymmetrical()
                .build();

            PolymerCoreApi.getInstance().getMultiblockManager().addDefinedMultiblock(build.setRegistryName(new ResourceLocation(PolymerCoreApi.MOD_ID, "test_machine")));


            IDefinedMultiblock build2 = new DefaultCharMarkedMultiblockBuilder()
                .addPattern(
                    "ACCCA",
                    "AAAAA",
                    "AABAD"
                )
                .addPattern(
                    "AAEEA",
                    "A B A",
                    "AEAAA"
                )
                .addPattern(
                    "     ",
                    "  B  ",
                    "     "
                )
                .addPartsMap('A', new PartSpecifiedBlock(Blocks.GOLD_BLOCK))
                .addPartsMap('B', new PartSpecifiedBlock(Blocks.OAK_WOOD))
                .addPartsMap('C', new PartSpecifiedBlock(Blocks.DIAMOND_BLOCK))
                .addPartsMap('D', new PartSpecifiedBlock(Blocks.GLOWSTONE))
                .addPartsMap('E', new PartSpecifiedBlock(Blocks.QUARTZ_STAIRS))

                .setCoreChar('D')
                .setType(MultiblockRegisterHandler.TYPE_FREE.get())
                .setCanSymmetrical()
                .build();

            PolymerCoreApi.getInstance().getMultiblockManager().addDefinedMultiblock(build2.setRegistryName(new ResourceLocation(PolymerCoreApi.MOD_ID, "test_machine_2")));



        }
    }

}
