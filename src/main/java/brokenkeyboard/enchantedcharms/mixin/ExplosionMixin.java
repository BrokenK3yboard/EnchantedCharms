package brokenkeyboard.enchantedcharms.mixin;

import brokenkeyboard.enchantedcharms.item.CharmItem;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;
import java.util.Optional;

import static brokenkeyboard.enchantedcharms.enchantment.copper.OrdnanceEnchantment.ORDNANCE_ENCH;

@Mixin(Explosion.class)
public class ExplosionMixin {

    @ModifyArg(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"), index = 1)
    private float changeExplosionDamage(DamageSource source, float damage, @Local(ordinal = 0) List<Entity> list) {
        if (!(source.getEntity() instanceof LivingEntity entity)) return damage;
        Optional<SlotResult> curio = CharmItem.getCurio(entity, ORDNANCE_ENCH);
        if (curio.isEmpty()) return damage;
        System.out.println(list.size());
        float multiplier = 1 + (0.05F * (list.size() - 1));
        return damage * multiplier;
    }
}