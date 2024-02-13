package brokenkeyboard.enchantedcharms.enchantment.copper;

import brokenkeyboard.enchantedcharms.EnchantedCharms;
import brokenkeyboard.enchantedcharms.enchantment.CharmEnchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.function.Predicate;

public class GildedEnchantment extends CharmEnchantment {

    public static final Predicate<ItemStack> GILDED_ENCH = stack -> (EnchantmentHelper.getTagEnchantmentLevel(EnchantedCharms.GILDED.get(), stack) > 0);

    public GildedEnchantment(EnchantmentCategory category) {
        super(category);
    }
}