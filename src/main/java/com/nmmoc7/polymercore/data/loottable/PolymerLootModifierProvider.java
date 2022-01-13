package com.nmmoc7.polymercore.data.loottable;

import com.nmmoc7.polymercore.common.loot.LootRegistries;
import com.nmmoc7.polymercore.common.loot.modifiers.SmeltingEnchantmentModifier;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.MatchTool;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class PolymerLootModifierProvider extends GlobalLootModifierProvider
{
    public PolymerLootModifierProvider(DataGenerator gen, String modid)
    {
        super(gen, modid);
    }
    @Override
    protected void start()
    {
        add("smelting", LootRegistries.SMELTING.get(), new SmeltingEnchantmentModifier(
            new ILootCondition[]{
                MatchTool.toolMatches(
                    ItemPredicate.Builder.item().hasEnchantment(
                        new EnchantmentPredicate(Enchantments.FLAMING_ARROWS, MinMaxBounds.IntBound.atLeast(1))))
                    .build()
            })
        );
    }
}
