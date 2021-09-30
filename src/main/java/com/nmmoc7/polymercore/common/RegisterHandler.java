package com.nmmoc7.polymercore.common;

import com.nmmoc7.polymercore.PolymerCore;
import com.nmmoc7.polymercore.common.event.DefaultTileRegEvent;
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
    public static final BlockItem ITEM = new BlockItem(BLOCK, new Item.Properties().group(PolymerItemGroup.INSTANCE));
    public static TileEntityType<PolymerCoreTileEntity> TILE;

    public static final ResourceLocation BLOCK_NAME = new ResourceLocation(PolymerCore.MOD_ID, "polymer_core_block");

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                BLOCK.setRegistryName(BLOCK_NAME)
        );
    }

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                ITEM.setRegistryName(BLOCK_NAME)
        );
    }

    @SubscribeEvent
    public static void onTileTypeRegister(RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().registerAll(
                TILE = (TileEntityType<PolymerCoreTileEntity>) TileEntityType.Builder.create(PolymerCoreTileEntity::new, getTileBlocks()).build(null)
                        .setRegistryName(new ResourceLocation(PolymerCore.MOD_ID, "polymer_core_tile"))
        );
    }

    @SubscribeEvent
    public static void onDefaultTileRegister(DefaultTileRegEvent event) {
        event.add(BLOCK);
    }

    /**
     * 如果想使用自带的tileentity，请在这个事件注册
     */
    static Block[] getTileBlocks() {
        DefaultTileRegEvent result = new DefaultTileRegEvent();
        ModLoader.get().postEvent(result);
        return result.getList().toArray(new Block[0]);
    }
}
