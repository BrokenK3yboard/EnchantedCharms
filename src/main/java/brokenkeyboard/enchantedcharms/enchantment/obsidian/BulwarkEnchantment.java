package brokenkeyboard.enchantedcharms.enchantment.obsidian;

import brokenkeyboard.enchantedcharms.EnchantedCharms;
import brokenkeyboard.enchantedcharms.enchantment.CharmEnchantment;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;
import java.util.function.Predicate;

import static brokenkeyboard.enchantedcharms.item.CharmItem.getCurio;

public class BulwarkEnchantment extends CharmEnchantment {

    public static final Predicate<ItemStack> BULWARK_ENCH = stack -> (EnchantmentHelper.getTagEnchantmentLevel(EnchantedCharms.BULWARK.get(), stack) > 0);

    public BulwarkEnchantment(EnchantmentCategory category) {
        super(category);
        MinecraftForge.EVENT_BUS.addListener(this::shieldBlock);
    }

    public void shieldBlock(ShieldBlockEvent event) {
        LivingEntity entity = event.getEntity();
        Optional<SlotResult> curio = getCurio(entity, BULWARK_ENCH);
        if (curio.isEmpty()) return;

        float damage = event.getBlockedDamage();

        if (damage <= 8) {
            event.setShieldTakesDamage(false);
        }

        if (event.getDamageSource().getEntity() instanceof LivingEntity attacker && attacker.getMainHandItem()
                .canDisableShield(entity.getUseItem(), entity, attacker) || damage > 8) {
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 0, true, true));
        }
    }
}