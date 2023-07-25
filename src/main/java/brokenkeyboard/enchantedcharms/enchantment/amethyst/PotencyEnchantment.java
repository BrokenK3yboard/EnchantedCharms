package brokenkeyboard.enchantedcharms.enchantment.amethyst;

import brokenkeyboard.enchantedcharms.EnchantedCharms;
import brokenkeyboard.enchantedcharms.enchantment.CharmEnchantment;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.PotionEvent;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;
import java.util.function.Predicate;

import static brokenkeyboard.enchantedcharms.item.CharmItem.getCurio;

public class PotencyEnchantment extends CharmEnchantment {

    public static final Predicate<ItemStack> POTENCY_ENCH = stack -> (EnchantmentHelper.getItemEnchantmentLevel(EnchantedCharms.POTENCY.get(), stack) > 0);

    public PotencyEnchantment(EnchantmentCategory category) {
        super(category);
        MinecraftForge.EVENT_BUS.addListener(this::increaseEffect);
    }

    public void increaseEffect(PotionEvent.PotionAddedEvent event) {
        LivingEntity entity = event.getEntityLiving();
        Optional<SlotResult> curio = getCurio(entity, POTENCY_ENCH);
        if (curio.isEmpty()) return;

        MobEffectInstance effect = event.getPotionEffect();
        MobEffect mobEffect = effect.getEffect();

        if (!mobEffect.isInstantenous() && mobEffect.isBeneficial()) {
            int duration = (int) (effect.getDuration() * 1.4);
            MobEffectInstance newEffect = new MobEffectInstance(mobEffect, duration, effect.getAmplifier(), effect.isAmbient(), effect.isVisible(), effect.showIcon());
            effect.update(newEffect);
        }
    }
}