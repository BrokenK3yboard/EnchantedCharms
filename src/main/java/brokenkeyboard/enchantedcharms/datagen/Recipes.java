package brokenkeyboard.enchantedcharms.datagen;

import brokenkeyboard.enchantedcharms.EnchantedCharms;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

    public Recipes(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        charmRecipe(EnchantedCharms.COPPER_CHARM.get(), Tags.Items.INGOTS_COPPER, consumer);
        charmRecipe(EnchantedCharms.OBSIDIAN_CHARM.get(), Tags.Items.OBSIDIAN, consumer);
        charmRecipe(EnchantedCharms.AMETHYST_CHARM.get(), Tags.Items.GEMS_AMETHYST, consumer);
        charmRecipe(EnchantedCharms.EMERALD_CHARM.get(), Tags.Items.GEMS_EMERALD, consumer);
    }

    protected void charmRecipe(Item output, TagKey<Item> item, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output)
                .define('I', item)
                .define('S', Tags.Items.STRING)
                .pattern("S")
                .pattern("I")
                .pattern("I")
                .unlockedBy("has_" + item.toString().toLowerCase(), has(item))
                .save(consumer);
    }
}