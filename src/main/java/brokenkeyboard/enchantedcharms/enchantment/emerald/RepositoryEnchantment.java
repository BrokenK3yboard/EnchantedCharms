package brokenkeyboard.enchantedcharms.enchantment.emerald;

import brokenkeyboard.enchantedcharms.EnchantedCharms;
import brokenkeyboard.enchantedcharms.enchantment.CharmEnchantment;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static brokenkeyboard.enchantedcharms.item.CharmItem.getCurio;

public class RepositoryEnchantment extends CharmEnchantment {

    public static final int MAX_XP = 1395;
    public static final Predicate<ItemStack> EXP_ENCH = stack -> (EnchantmentHelper.getItemEnchantmentLevel(EnchantedCharms.REPOSITORY.get(), stack) > 0);
    public static final Predicate<ItemStack> EXP_ENCH_FILLED = stack -> EXP_ENCH.test(stack) && getStoredXP(stack) > 0;
    public static final Predicate<ItemStack> EXP_ENCH_MAX = stack -> EXP_ENCH.test(stack) && getStoredXP(stack) < MAX_XP;

    public RepositoryEnchantment(EnchantmentCategory category) {
        super(category);
        MinecraftForge.EVENT_BUS.addListener(this::enchantmentEffect);
    }

    public void enchantmentEffect(PlayerXpEvent.PickupXp event) {
        Player player = event.getPlayer();
        Optional<SlotResult> curio = getCurio(player, EXP_ENCH_MAX);
        if (curio.isEmpty()) return;

        ItemStack stack = curio.get().stack();
        int charm_xp = stack.getOrCreateTag().getInt("xp");
        int xp = event.getOrb().getValue();

        if (charm_xp + xp > MAX_XP) {
            stack.getOrCreateTag().putInt("xp", MAX_XP);
            int extra = charm_xp + xp - MAX_XP;
            player.giveExperiencePoints(extra);
        } else {
            stack.getOrCreateTag().putInt("xp", charm_xp + xp);
        }
        event.getOrb().value = 0;
    }

    public static int getStoredXP(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag == null ? 0 : tag.getInt("xp");
    }

    public static int getLevels(int value) {
        if (value >= 1508) {
            return (int) Math.floor(325D / 18 + Math.sqrt(2D / 9 + (value - (54217D / 72))));
        } else {
            return value >= 353 ? (int) Math.floor(8.1 + Math.sqrt(0.4 * (value - (7839D / 40)))) : (int) Math.floor(Math.sqrt(value + 9) - 3);
        }
    }

    public static int getXPFromLevels(int value) {
        if (value >= 32) {
            return (int) (4.5 * Math.pow(value, 2) - 162.5 * value + 2220);
        } else {
            return value >= 17 ? (int) (2.5 * Math.pow(value, 2) - 40.5 * value + 360) : (int) (Math.pow(value, 2) + 6 * value);
        }
    }

    public static void getHoverText(List<Component> components, ItemStack stack) {
        if (EXP_ENCH_FILLED.test(stack)) {
            int storedXP = getStoredXP(stack);
            int storedLevels = getLevels(storedXP);
            components.add(new TranslatableComponent("enchantedcharms.repository_xp", String.valueOf(storedLevels), String.valueOf(storedXP), MAX_XP).withStyle(ChatFormatting.BLUE));
        }
    }
}