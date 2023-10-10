package brokenkeyboard.enchantedcharms.enchantment;

import brokenkeyboard.enchantedcharms.enchantment.emerald.RepositoryEnchantment;
import brokenkeyboard.enchantedcharms.enchantment.obsidian.FocusEnchantment;
import brokenkeyboard.enchantedcharms.enchantment.obsidian.HuntersMarkEnchantment;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.GrindstoneEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommonEvents {

    @SubscribeEvent
    public static void applyDamageBonus(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        LivingEntity attacker = getAttacker(source);
        if (attacker == null) return;

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

        FocusEnchantment.addStacks(attacker, victim);
        HuntersMarkEnchantment.applyGlowing(source, attacker, victim);
    }

    @SubscribeEvent
    public static void grindstoneCharm(GrindstoneEvent.OnPlaceItem event) {
        if (RepositoryEnchantment.EXP_ENCH_FILLED.test(event.getTopItem()) && event.getBottomItem().isEmpty()) {
            resetXP(event, event.getTopItem().copy());
        } else if (RepositoryEnchantment.EXP_ENCH_FILLED.test(event.getTopItem()) && event.getBottomItem().isEmpty()) {
            resetXP(event, event.getBottomItem().copy());
        }
    }

    public static void resetXP(GrindstoneEvent.OnPlaceItem event, ItemStack stack) {
        event.setXp(RepositoryEnchantment.getStoredXP(stack));
        stack.getOrCreateTag().putInt("xp", 0);
        event.setOutput(stack);
    }

    public static LivingEntity getAttacker(DamageSource source) {
        if (source.getEntity() instanceof LivingEntity entity) return entity;
        return null;
    }
}