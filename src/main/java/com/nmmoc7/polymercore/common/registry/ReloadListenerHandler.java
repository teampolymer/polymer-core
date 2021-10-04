package com.nmmoc7.polymercore.common.registry;

import com.nmmoc7.polymercore.PolymerCore;
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

@Mod.EventBusSubscriber(modid = PolymerCore.MOD_ID)
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
                    "AABAA",
                    "AAAAA"
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

            MultiblockManagerImpl.INSTANCE.get().addDefinedMultiblock(build.setRegistryName(new ResourceLocation(PolymerCore.MOD_ID, "test_machine")));
        }
    }

}
