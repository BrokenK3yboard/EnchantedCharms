package brokenkeyboard.enchantedcharms.enchantment.obsidian;

import brokenkeyboard.enchantedcharms.EnchantedCharms;
import brokenkeyboard.enchantedcharms.enchantment.CharmEnchantment;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;
import java.util.function.Predicate;

import static brokenkeyboard.enchantedcharms.item.CharmItem.getCurio;

public class HuntersMarkEnchantment extends CharmEnchantment {

    public static final Predicate<ItemStack> PROJECTILE_ENCH = stack -> (EnchantmentHelper.getTagEnchantmentLevel(EnchantedCharms.HUNTERS_MARK.get(), stack) > 0);

    public HuntersMarkEnchantment(EnchantmentCategory category) {
        super(category);
    }

    public static double damageBonus(LivingEntity attacker, LivingEntity victim) {
        Optional<SlotResult> curio = getCurio(attacker, PROJECTILE_ENCH);
        if (curio.isEmpty()) return 0;

        return victim.hasEffect(MobEffects.GLOWING) ? 0.2 : 0;
    }

    public static void applyGlowing(DamageSource source, LivingEntity attacker, LivingEntity victim) {
        Optional<SlotResult> curio = getCurio(attacker, PROJECTILE_ENCH);
        if (curio.isEmpty()) return;

        if (source.isProjectile() && victim.getRandom().nextDouble() < 0.4)
            victim.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0, true, true));
    }
}