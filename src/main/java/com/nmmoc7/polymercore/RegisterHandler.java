package com.nmmoc7.polymercore;

import com.nmmoc7.polymercore.block.PolymerCoreBlock;
import com.nmmoc7.polymercore.block.PolymerCoreBlueprintBlock;
import com.nmmoc7.polymercore.event.DefaultTileRegEvent;
import com.nmmoc7.polymercore.tileentity.PolymerCoreTileEntity;
import com.nmmoc7.polymercore.tileentity.blueprint.PolymerCoreBlueprintTileEntity;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterHandler {
    public static final PolymerCoreBlock BLOCK = new PolymerCoreBlock();
    public static final PolymerCoreBlueprintBlock BLUEPRINT_BLOCK = new PolymerCoreBlueprintBlock();
    public static final BlockItem ITEM = new BlockItem(BLOCK, new Item.Properties().group(PolymerItemGroup.INSTANCE));
    public static final BlockItem BLUEPRINT_ITEM = new BlockItem(BLUEPRINT_BLOCK, new Item.Properties().group(PolymerItemGroup.INSTANCE));
    public static TileEntityType<PolymerCoreTileEntity> TILE;
    public static TileEntityType<PolymerCoreBlueprintTileEntity> BLUEPRINT_TILE;

    public static final ResourceLocation BLOCK_NAME = new ResourceLocation(PolymerCore.MOD_ID, "polymer_core_block");
    public static final ResourceLocation BLUEPRINT_BLOCK_NAME = new ResourceLocation(PolymerCore.MOD_ID, "polymer_core_blueprint_block");

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                BLOCK.setRegistryName(BLOCK_NAME),
                BLUEPRINT_BLOCK.setRegistryName(BLUEPRINT_BLOCK_NAME)
        );
    }

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                ITEM.setRegistryName(BLOCK_NAME),
                BLUEPRINT_ITEM.setRegistryName(BLUEPRINT_BLOCK_NAME)
        );
    }

    @SubscribeEvent
    public static void onTileTypeRegister(RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().registerAll(
                TILE = (TileEntityType<PolymerCoreTileEntity>) TileEntityType.Builder.create(PolymerCoreTileEntity::new, getTileBlocks()).build(null)
                        .setRegistryName(new ResourceLocation(PolymerCore.MOD_ID, BLOCK_NAME + "_tile")),
                BLUEPRINT_TILE = (TileEntityType<PolymerCoreBlueprintTileEntity>) TileEntityType.Builder.create(PolymerCoreBlueprintTileEntity::new, getBlueprintTileBlocks()).build(null)
                        .setRegistryName(new ResourceLocation(PolymerCore.MOD_ID, BLUEPRINT_BLOCK_NAME + "_tile"))
        );
    }

    @SubscribeEvent
    public static void onDefaultTileRegister(DefaultTileRegEvent event) {
        if (event.getType() == TILE)
            event.addAll(BLOCK);
        if (event.getType() == BLUEPRINT_TILE)
            event.addAll(BLUEPRINT_BLOCK);
    }

    /**
     * 如果想使用自带的tileentity，请在这个事件注册
     */
    static Block[] getTileBlocks() {
        DefaultTileRegEvent result = new DefaultTileRegEvent(TILE);
        ModLoader.get().postEvent(result);
        return result.getList().toArray(new Block[0]);
    }

    static Block[] getBlueprintTileBlocks() {
        DefaultTileRegEvent result = new DefaultTileRegEvent(BLUEPRINT_TILE);
        ModLoader.get().postEvent(result);
        return result.getList().toArray(new Block[0]);
    }
}
