package brokenkeyboard.enchantedcharms.datagen;

import brokenkeyboard.enchantedcharms.EnchantedCharms;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ItemTags extends ItemTagsProvider {

    public ItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, @Nullable ExistingFileHelper fileHelper) {
        super(output, provider, blockTags, EnchantedCharms.MOD_ID, fileHelper);
    }

    @Override
    public @NotNull String getName() {
        return "Charms item tags";
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(EnchantedCharms.CHARM).add(EnchantedCharms.COPPER_CHARM.get());
        tag(EnchantedCharms.CHARM).add(EnchantedCharms.OBSIDIAN_CHARM.get());
        tag(EnchantedCharms.CHARM).add(EnchantedCharms.AMETHYST_CHARM.get());
        tag(EnchantedCharms.CHARM).add(EnchantedCharms.EMERALD_CHARM.get());
    }
}
