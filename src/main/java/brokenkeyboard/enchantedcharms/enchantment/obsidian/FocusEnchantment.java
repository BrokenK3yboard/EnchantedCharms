package brokenkeyboard.enchantedcharms.enchantment.obsidian;

import brokenkeyboard.enchantedcharms.EnchantedCharms;
import brokenkeyboard.enchantedcharms.enchantment.CharmEnchantment;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static brokenkeyboard.enchantedcharms.item.CharmItem.getCurio;

public class FocusEnchantment extends CharmEnchantment {

    public static final Predicate<ItemStack> FOCUS_ENCH = stack -> (EnchantmentHelper.getItemEnchantmentLevel(EnchantedCharms.FOCUS.get(), stack) > 0);

    public FocusEnchantment(EnchantmentCategory category) {
        super(category);
    }

    public static double damageBonus(LivingEntity attacker) {
        Optional<SlotResult> curio = getCurio(attacker, FOCUS_ENCH);
        return curio.map(slotResult -> getStacks(slotResult.stack()) * 0.06).orElse(0.0);
    }

    public static void addStacks(LivingEntity attacker, LivingEntity victim) {
        Optional<SlotResult> curioAttacker = getCurio(attacker, FOCUS_ENCH);
        Optional<SlotResult> curioVictim = getCurio(victim, FOCUS_ENCH);

        if (curioAttacker.isPresent()) {
            ItemStack stack = curioAttacker.get().stack();
            setStacks(stack, getStacks(stack) + 1);
        }

        if (curioVictim.isPresent()) {
            ItemStack stack = curioVictim.get().stack();
            setStacks(stack, getStacks(stack) - 1);
        }
    }

    public static void getHoverText(List<Component> components, ItemStack stack) {
        if (FOCUS_ENCH.test(stack) && getStacks(stack) > 0) {
            components.add(new TextComponent("+" + (getStacks(stack) * 6) + "% ")
                    .append(new TranslatableComponent("attribute.name.generic.attack_damage"))
                    .withStyle(ChatFormatting.BLUE));
        }
    }

    public static void setStacks(ItemStack stack, int amount) {
        amount = Math.min(amount, 5);
        amount = Math.max(amount, 0);
        stack.getOrCreateTag().putInt("stacks", amount);
    }

    public static int getStacks(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag == null ? 0 : tag.getInt("stacks");
    }
}