package com.teampolymer.polymer.core.client.utils.schematic.control;

import com.teampolymer.polymer.core.api.capability.IMultiblockLocateHandler;
import com.teampolymer.polymer.core.client.resources.IGuiResource;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.BlockPos;

public abstract class MoveAbstract extends ControlAction {
    public MoveAbstract(IGuiResource icon) {
        super(icon);
    }

    public abstract BlockPos doMove(BlockPos current, int move);

    @Override
    public boolean handleMouseScrolled(IMultiblockLocateHandler locateHandler, double delta) {
        if (Screen.hasAltDown() && locateHandler.isAnchored()) {
            int move = (int) delta;
            BlockPos offset = locateHandler.getOffset();

            if (move != 0 && offset != null) {
                locateHandler.setOffset(doMove(offset, (int) delta));
                callback.run();
            }
            return true;
        }
        return false;
    }

    @Override
    public void update(IMultiblockLocateHandler locateHandler) {
        if (locateHandler.isAnchored() != isEnabled()) {
            setEnabled(locateHandler.isAnchored());
        }
    }
}
