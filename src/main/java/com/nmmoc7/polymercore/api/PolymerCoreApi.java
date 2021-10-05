package com.nmmoc7.polymercore.api;

import net.minecraftforge.common.util.Lazy;
import org.apache.logging.log4j.LogManager;

public interface PolymerCoreApi {

    Lazy<PolymerCoreApi> INSTANCE = Lazy.of(() -> {
        try {
            //TODO: 实现
            return (PolymerCoreApi) Class.forName("").newInstance();
        } catch (ReflectiveOperationException e) {
            LogManager.getLogger().warn("Unable to find BotaniaAPIImpl, using a dummy");
            return new PolymerCoreApi() {
            };
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

    default int apiVersion() {
        return 0;
    }




}
