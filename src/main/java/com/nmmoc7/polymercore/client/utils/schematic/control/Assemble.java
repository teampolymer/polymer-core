package com.nmmoc7.polymercore.client.utils.schematic.control;

import com.nmmoc7.polymercore.api.capability.IMultiblockLocateHandler;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.client.handler.MultiblockSchematicHandler;
import com.nmmoc7.polymercore.client.resources.Icons;
import com.nmmoc7.polymercore.common.network.ModNetworking;
import com.nmmoc7.polymercore.common.network.PacketAssembleMultiblock;
import com.nmmoc7.polymercore.common.network.PacketLocateHandlerSync;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.Objects;

public class Assemble extends ControlAction {

    public Assemble() {
        super(Icons.HAMMER);
    }

    @Override
    public ITextComponent getName() {
        return new TranslationTextComponent("gui.polymer.locator.control.assemble.title");
    }

    @Override
    public List<ITextComponent> getDescription() {
        return super.getDescription();
    }

    @Override
    public void handleMouseInput(IMultiblockLocateHandler locateHandler, int mouseButton, boolean pressed) {
        if (!pressed || mouseButton != 1) {
            return;
        }
        IDefinedMultiblock currentMultiblock = MultiblockSchematicHandler.INSTANCE.getCurrentMultiblock();
        if (currentMultiblock != null && currentMultiblock.getRegistryName() != null) {
            ModNetworking.INSTANCE.sendToServer(new PacketAssembleMultiblock(currentMultiblock.getRegistryName().toString(), locateHandler));
        }

    }
}
