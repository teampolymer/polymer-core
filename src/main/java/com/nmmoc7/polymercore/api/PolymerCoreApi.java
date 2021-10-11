package com.nmmoc7.polymercore.api;

import com.nmmoc7.polymercore.api.capability.IChunkMultiblockStorage;
import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import com.nmmoc7.polymercore.api.multiblock.assembled.IFreeMultiblock;
import com.nmmoc7.polymercore.api.registry.IMultiblockDefinitionManager;
import com.nmmoc7.polymercore.api.stub.StubPolymerCoreApi;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Lazy;
import org.apache.logging.log4j.LogManager;

import java.util.Collection;
import java.util.UUID;

public interface PolymerCoreApi {

    Lazy<PolymerCoreApi> INSTANCE = Lazy.of(() -> {
        try {
            //TODO: 实现
            return (PolymerCoreApi) Class.forName("com.nmmoc7.polymercore.common.PolymerCoreApiImpl").newInstance();
        } catch (ReflectiveOperationException e) {
            LogManager.getLogger().warn("Unable to find PolymerCoreApiImpl, using a dummy");
            return new StubPolymerCoreApi();
        }
    });


    String MOD_ID = "polymer-core";

    /**
     * 获取API实例对象
     *
     * @return
     */
    static PolymerCoreApi getInstance() {
        return INSTANCE.get();
    }

    /**
     * API版本
     *
     * @return
     */
    default int apiVersion() {
        return 0;
    }

    IMultiblockDefinitionManager getMultiblockManager();

    Capability<IChunkMultiblockStorage> getChunkMultiblockCapability();

    //---------------------------------------
    // Multiblock API
    //---------------------------------------

    IAssembledMultiblock findMultiblock(World world, UUID id);

    IAssembledMultiblock findMultiblock(World world, BlockPos pos, boolean coreBlock);

    IAssembledMultiblock findMultiblock(World world, BlockPos pos);

    Collection<IFreeMultiblock> findFreeMultiblocks(World world);



}
