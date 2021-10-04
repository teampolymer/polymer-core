package com.nmmoc7.polymercore.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import com.nmmoc7.polymercore.api.multiblock.assembled.IFreeMultiblock;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockPart;
import com.nmmoc7.polymercore.common.capability.chunk.CapabilityChunkMultiblockStorage;
import com.nmmoc7.polymercore.common.world.FreeMultiblockWorldSavedData;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.nio.file.spi.FileSystemProvider;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class MultiblockDebugCommand {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleCommandExceptionType NOT_RUNNING_EXCEPTION = new SimpleCommandExceptionType(new TranslationTextComponent("commands.debug.notRunning"));
    private static final SimpleCommandExceptionType ALREADY_RUNNING_EXCEPTION = new SimpleCommandExceptionType(new TranslationTextComponent("commands.debug.alreadyRunning"));

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("multiblock").requires(it -> {
                return it.hasPermissionLevel(3);
            }).then(Commands.literal("checkall").executes(it -> {
                return checkAll(it.getSource());
            }))
        );
    }

    private static int checkAll(CommandSource source) throws CommandSyntaxException {
        MinecraftServer server = source.getServer();
        ServerWorld world = source.getWorld();

        Collection<IAssembledMultiblock> multiblocks = FreeMultiblockWorldSavedData.get(world).getAssembledMultiblocks();
        for (IAssembledMultiblock multiblock : multiblocks) {
            if (multiblock instanceof IFreeMultiblock) {
//                Collection<ChunkPos> crossedChunks = ((IFreeMultiblock) multiblock).getCrossedChunks();
//                for (ChunkPos chunkPos : crossedChunks) {
//                    Chunk chunk = world.getChunk(chunkPos.x, chunkPos.z);
//                    chunk.getCapability(CapabilityChunkMultiblockStorage.MULTIBLOCK_STORAGE).ifPresent(it->{
//                        Map<BlockPos, Tuple<UUID, IMultiblockPart>> data = it.getData();
//                    });
//                }
                for (BlockPos pos : multiblock.getParts().keySet()) {
                    Tuple<UUID, IMultiblockPart> multiblockPart = CapabilityChunkMultiblockStorage.getMultiblockPart(world, pos);
                    if (multiblockPart == null || multiblockPart.getA() != multiblock.getMultiblockId()) {
                        source.sendFeedback(new StringTextComponent("Found an invalid multiblock :" + multiblock.getMultiblockId()), true);
                    }
                }
            }
        }
        source.sendFeedback(new StringTextComponent("Check fininshed"), true);
        return 0;
    }
}
