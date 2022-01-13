package com.nmmoc7.polymercore.common.event;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.common.multiblock.builder.JsonMultiblockBuilder;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IResourceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import net.minecraft.resources.IFutureReloadListener.IStage;

public class MultiblockReloadListener implements IFutureReloadListener {
    public static final Path MULTIBLOCK_DIR = Paths.get("polymer", "multiblock");

    @Override
    public CompletableFuture<Void> reload(IStage stage, IResourceManager resourceManager, IProfiler preparationsProfiler, IProfiler reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {

        return stage.wait(Void.TYPE)
            .thenRunAsync(() -> {
                    try {
                        doReload(resourceManager, reloadProfiler);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                , gameExecutor);
    }


    void doReload(IResourceManager resourceManager, IProfiler profiler) throws IOException {
        if (!Files.exists(MULTIBLOCK_DIR)) {
            Files.createDirectories(MULTIBLOCK_DIR);
        }
        List<IDefinedMultiblock> multiblocks = new ArrayList<>();
        Files.walkFileTree(MULTIBLOCK_DIR, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.getFileName().toString().endsWith(".json")) {
                    try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                        IDefinedMultiblock multiblock = new JsonMultiblockBuilder(reader).build();
                        multiblocks.add(multiblock);
                    }

                }
                return FileVisitResult.CONTINUE;
            }
        });

        for (IDefinedMultiblock multiblock : multiblocks) {
            PolymerCoreApi.getInstance().getMultiblockManager().register(multiblock);
        }
    }

}
