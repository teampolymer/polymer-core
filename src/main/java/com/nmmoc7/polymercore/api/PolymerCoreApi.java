package com.nmmoc7.polymercore.api;

import com.nmmoc7.polymercore.api.capability.IChunkMultiblockStorage;
import com.nmmoc7.polymercore.api.registry.IMultiblockDefinitionManager;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Lazy;
import org.apache.logging.log4j.LogManager;

public interface PolymerCoreApi {

    Lazy<PolymerCoreApi> INSTANCE = Lazy.of(() -> {
        try {
            //TODO: 实现
            return (PolymerCoreApi) Class.forName("").newInstance();
        } catch (ReflectiveOperationException e) {
            LogManager.getLogger().warn("Unable to find BotaniaAPIImpl, using a dummy");
            return null;
        }
    });


    String MOD_ID = "polymer-core";

    /**
     * 获取API实例对象
     *
     * @return
     */
    static PolymerCoreApi instance() {
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




}
