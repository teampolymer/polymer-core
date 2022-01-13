package com.nmmoc7.polymercore.common.item;

import com.nmmoc7.polymercore.api.PolymerCoreApi;
import com.nmmoc7.polymercore.api.multiblock.MultiblockDirection;
import com.nmmoc7.polymercore.common.PolymerItemGroup;
import com.nmmoc7.polymercore.common.block.ModBlocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, PolymerCoreApi.MOD_ID);


    public static final RegistryObject<Item> BLUEPRINT = REGISTER.register("blueprint", BlueprintItem::new);


    public static final RegistryObject<Item> TEST_BLOCK = REGISTER.register("test_block", () -> new BlockItem(ModBlocks.TestBlock.get(), (new Item.Properties()).stacksTo(1).tab(PolymerItemGroup.INSTANCE)));
}
