package com.teampolymer.polymer.core.client.utils.schematic.control;

import com.google.common.collect.Lists;
import com.teampolymer.polymer.core.api.capability.IMultiblockLocateHandler;
import com.teampolymer.polymer.core.api.multiblock.IArchetypeMultiblock;
import com.teampolymer.polymer.core.client.handler.MultiblockSchematicHandler;
import com.teampolymer.polymer.core.client.resources.Icons;
import com.teampolymer.polymer.core.common.network.ModNetworking;
import com.teampolymer.polymer.core.common.network.PacketAssembleMultiblock;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collections;
import java.util.List;

public class Assemble extends ControlAction {

    public Assemble() {
        super(Icons.HAMMER);
    }

    @Override
    public ITextComponent getName() {
        return new TranslationTextComponent("gui.polymer.locator.control.assemble.title");
    }

    private final List<ITextComponent> description = Lists.newArrayList(
        new TranslationTextComponent("gui.polymer.locator.control.assemble.description_1"),
        new TranslationTextComponent("gui.polymer.locator.control.assemble.description_2"),
        new TranslationTextComponent("gui.polymer.locator.control.assemble.description_3")
    );

    @Override
    public List<ITextComponent> getDescription() {
        return Collections.unmodifiableList(description);
    }

    @Override
    public boolean shouldHideSchematic() {
        return true;
    }

    @Override
    public void handleMouseInput(IMultiblockLocateHandler locateHandler, int mouseButton, boolean pressed) {
        if (!pressed || mouseButton != 1) {
            return;
        }
        IArchetypeMultiblock currentMultiblock = MultiblockSchematicHandler.INSTANCE.getCurrentMultiblock();
        Minecraft mc = Minecraft.getInstance();
        if (mc.hitResult instanceof BlockRayTraceResult && mc.hitResult.getType() == RayTraceResult.Type.BLOCK) {
            BlockPos pos = ((BlockRayTraceResult) mc.hitResult).getBlockPos();
            if (currentMultiblock != null && currentMultiblock.getRegistryName() != null) {
                ModNetworking.INSTANCE.sendToServer(new PacketAssembleMultiblock(currentMultiblock.getRegistryName().toString(), pos));
            }
        }

    }
}
