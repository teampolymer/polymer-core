package com.nmmoc7.polymercore.client.utils.schematic;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.client.renderer.CustomRenderTypeBuffer;
import com.nmmoc7.polymercore.client.utils.animate.AnimatedSchematicTransformation;

public abstract class SchematicRenderer {
    private AnimatedSchematicTransformation transformation;

    public abstract void setMultiblock(IDefinedMultiblock multiblock);
    public abstract void render(MatrixStack ms, CustomRenderTypeBuffer buffer, float pt);
}
