package brokenkeyboard.enchantedcharms.datagen;

import brokenkeyboard.enchantedcharms.EnchantedCharms;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.AlternativeLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class GLMProvider extends GlobalLootModifierProvider {
    public GLMProvider(DataGenerator generator) {
        super(generator, EnchantedCharms.MOD_ID);
    }

    @Override
    protected void start() {
        add("fishing_loot", EnchantedCharms.FISHING_LOOT.get(), new FishingLoot(new LootItemCondition[] {
                new AlternativeLootItemCondition.Builder(new LootTableIdCondition.Builder(new ResourceLocation("minecraft:gameplay/fishing"))).build()}));

        add("gilded_loot", EnchantedCharms.GILDED_LOOT.get(), new GildedLoot(new LootItemCondition[] {}));
    }
}