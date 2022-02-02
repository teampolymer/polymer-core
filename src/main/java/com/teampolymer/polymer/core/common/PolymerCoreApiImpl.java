package com.teampolymer.polymer.core.common;

import com.teampolymer.polymer.core.api.PolymerCoreApi;
import com.teampolymer.polymer.core.api.manager.IWorldMultiblockManager;
import com.teampolymer.polymer.core.api.manager.IArchetypeMultiblockManager;
import com.teampolymer.polymer.core.common.multiblock.EmptyWorldMultiblockManager;
import com.teampolymer.polymer.core.common.registry.MultiblockManagerImpl;
import net.minecraftforge.common.util.Lazy;

public class PolymerCoreApiImpl implements PolymerCoreApi {
    private final Lazy<IArchetypeMultiblockManager> multiblockDefinitionManager = Lazy.of(MultiblockManagerImpl::new);



    private IWorldMultiblockManager worldMultiblockManager = null;
    public void setWorldMultiblockManager(IWorldMultiblockManager worldMultiblockManager) {
        this.worldMultiblockManager = worldMultiblockManager;
    }

    @Override
    public IArchetypeMultiblockManager getArchetypeManager() {
        return multiblockDefinitionManager.get();
    }

    @Override
    public IWorldMultiblockManager getWorldMultiblockManager() {
        if (worldMultiblockManager == null) {
            worldMultiblockManager = new EmptyWorldMultiblockManager();
        }
        return worldMultiblockManager;
    }

}
