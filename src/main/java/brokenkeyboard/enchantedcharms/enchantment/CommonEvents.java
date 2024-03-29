package brokenkeyboard.enchantedcharms.enchantment;

import brokenkeyboard.enchantedcharms.enchantment.obsidian.FocusEnchantment;
import brokenkeyboard.enchantedcharms.enchantment.obsidian.HuntersMarkEnchantment;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommonEvents {

    @SubscribeEvent
    public static void applyDamageBonus(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        LivingEntity attacker = getAttacker(source);
        if (attacker == null || !(source.is(DamageTypeTags.IS_PROJECTILE) || source.getDirectEntity() instanceof Player)) return;

        LivingEntity victim = event.getEntity();
        double bonus = 1;

        bonus += HuntersMarkEnchantment.damageBonus(attacker, victim);
        bonus += FocusEnchantment.damageBonus(attacker);

        event.setAmount((float) (event.getAmount() * bonus));
    }

    @SubscribeEvent
    public static void applyDamageEffects(LivingDamageEvent event) {
        DamageSource source = event.getSource();
        LivingEntity attacker = getAttacker(event.getSource());

        LivingEntity victim = event.getEntity();

        FocusEnchantment.addStacks(source, attacker, victim);
        HuntersMarkEnchantment.applyGlowing(source, attacker, victim);
    }

    public static LivingEntity getAttacker(DamageSource source) {
        if (source.getEntity() instanceof LivingEntity entity) return entity;
        return null;
    }
}