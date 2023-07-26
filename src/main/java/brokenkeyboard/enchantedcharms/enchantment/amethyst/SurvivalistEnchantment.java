package brokenkeyboard.enchantedcharms.enchantment.amethyst;

import brokenkeyboard.enchantedcharms.EnchantedCharms;
import brokenkeyboard.enchantedcharms.enchantment.CharmEnchantment;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;
import java.util.function.Predicate;

import static brokenkeyboard.enchantedcharms.item.CharmItem.getCurio;

public class SurvivalistEnchantment extends CharmEnchantment {

    public static final Predicate<ItemStack> SAVORY_ENCH = stack -> (EnchantmentHelper.getTagEnchantmentLevel(EnchantedCharms.SURVIVALIST.get(), stack) > 0);

    public SurvivalistEnchantment(EnchantmentCategory category) {
        super(category);
        MinecraftForge.EVENT_BUS.addListener(this::healPlayer);
    }

    public void healPlayer(LivingEntityUseItemEvent.Finish event) {
        ItemStack stack = event.getItem();
        if (!stack.isEdible() || !(event.getEntity() instanceof Player player)) return;
        Optional<SlotResult> curio = getCurio(player, SAVORY_ENCH);
        if (curio.isEmpty()) return;

        player.getFoodData().setSaturation((float) (player.getFoodData().getSaturationLevel() + (stack.getFoodProperties(player).getSaturationModifier() * 0.4)));
        player.heal(stack.getFoodProperties(player).getNutrition() * 0.4F);
    }
}