package com.nmmoc7.polymercore.client.event.speciarender;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.client.event.SchematicSpecialRenderEvent;
import com.nmmoc7.polymercore.client.renderer.CustomRenderTypes;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.block.*;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.DualBrightnessCallback;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = PolymerCoreApi.MOD_ID)
@OnlyIn(Dist.CLIENT)
public class VanillaRenderHandler {
    @SubscribeEvent
    public static void onSpecialRender(SchematicSpecialRenderEvent event) {
        if (event.state.getBlock() instanceof AbstractChestBlock<?>) {
            renderChest(event.state, event.ms, event.combinedLight, event.bufferSource, event.isDynamic);
            event.setCanceled(true);
        }

    }


    private static final ModelRenderer chestSingleLid;
    private static final ModelRenderer chestSingleBottom;
    private static final ModelRenderer chestSingleLatch;
    private static final RenderType chestRender;
    private static final RenderType chestRenderDynamic;

    static {
        chestSingleBottom = new ModelRenderer(64, 64, 0, 19);
        chestSingleBottom.addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F, 0.0F);
        chestSingleLid = new ModelRenderer(64, 64, 0, 0);
        chestSingleLid.addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F, 0.0F);
        chestSingleLid.y = 9.0F;
        chestSingleLid.z = 1.0F;
        chestSingleLatch = new ModelRenderer(64, 64, 0, 0);
        chestSingleLatch.addBox(7.0F, -1.0F, 15.0F, 2.0F, 4.0F, 1.0F, 0.0F);
        chestSingleLatch.y = 8.0F;

        chestRender = CustomRenderTypes.getEntityTransparent(Atlases.CHEST_LOCATION.atlasLocation());
        chestRenderDynamic = CustomRenderTypes.getEntityTransparentDynamic(Atlases.CHEST_LOCATION.atlasLocation());
    }

    public static void renderChest(BlockState blockState, MatrixStack ms, int combinedLightIn, IRenderTypeBuffer bufferSource, boolean isDynamic) {
        Block block = blockState.getBlock();
        if (block instanceof AbstractChestBlock) {
            ms.pushPose();
            float f = blockState.getValue(ChestBlock.FACING).toYRot();
            ms.translate(0.5D, 0.5D, 0.5D);
            ms.mulPose(Vector3f.YP.rotationDegrees(-f));
            ms.translate(-0.5D, -0.5D, -0.5D);

            float lidAngle = 0f;
            RenderMaterial rendermaterial = Atlases.CHEST_LOCATION;
            IVertexBuilder buffer = rendermaterial.sprite().wrap(
                bufferSource.getBuffer(isDynamic ? chestRenderDynamic : chestRender)
            );

            chestSingleLid.xRot = -(lidAngle * ((float) Math.PI / 2F));
            chestSingleLatch.xRot = chestSingleLid.xRot;
            chestSingleLid.render(ms, buffer, combinedLightIn, OverlayTexture.NO_OVERLAY);
            chestSingleLatch.render(ms, buffer, combinedLightIn, OverlayTexture.NO_OVERLAY);
            chestSingleBottom.render(ms, buffer, combinedLightIn, OverlayTexture.NO_OVERLAY);

            ms.popPose();
        }

    }
}