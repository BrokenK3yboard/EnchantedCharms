package brokenkeyboard.enchantedcharms.datagen;

import brokenkeyboard.enchantedcharms.EnchantedCharms;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class GLMProvider extends GlobalLootModifierProvider {
    public GLMProvider(PackOutput output) {
        super(output, EnchantedCharms.MOD_ID);
    }

    @Override
    protected void start() {
        add("fishing_loot", new FishingLoot(new LootItemCondition[] {
                new AnyOfCondition.Builder(new LootTableIdCondition.Builder(new ResourceLocation("minecraft:gameplay/fishing"))).build()
        }));

        add("gilded_loot", new GildedLoot(new LootItemCondition[] {}));
    }
}