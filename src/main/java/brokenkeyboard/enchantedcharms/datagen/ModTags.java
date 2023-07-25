package brokenkeyboard.enchantedcharms.datagen;

import brokenkeyboard.enchantedcharms.EnchantedCharms;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModTags extends ItemTagsProvider {

    public ModTags(DataGenerator generator, BlockTagsProvider provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, provider, EnchantedCharms.MOD_ID, existingFileHelper);
    }

    @Override
    public @NotNull String getName() {
        return "Charms item tags";
    }

    @Override
    protected void addTags() {
        tag(EnchantedCharms.CHARM).add(EnchantedCharms.COPPER_CHARM.get());
        tag(EnchantedCharms.CHARM).add(EnchantedCharms.OBSIDIAN_CHARM.get());
        tag(EnchantedCharms.CHARM).add(EnchantedCharms.AMETHYST_CHARM.get());
        tag(EnchantedCharms.CHARM).add(EnchantedCharms.EMERALD_CHARM.get());
    }
}
