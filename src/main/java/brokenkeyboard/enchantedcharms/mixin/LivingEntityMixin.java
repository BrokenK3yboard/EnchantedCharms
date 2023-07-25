package brokenkeyboard.enchantedcharms.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;
import java.util.function.Predicate;

import static brokenkeyboard.enchantedcharms.enchantment.amethyst.AntidoteEnchantment.*;
import static brokenkeyboard.enchantedcharms.item.CharmItem.getCurio;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    private static final Predicate<ItemStack> EMPTY_RESIST = stack -> ANTIDOTE_ENCH.test(stack) && getPotionEffect(stack) == null;

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
    public void addEffect(MobEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = ((LivingEntity) (Object) this);
        Predicate<ItemStack> hasResist = stack -> getPotionEffect(stack) != null && getPotionEffect(stack).getEffect() == effect.getEffect();
        Optional<SlotResult> emptyResist = getCurio(entity, EMPTY_RESIST);
        Optional<SlotResult> resistResult = getCurio(entity, hasResist);

        if (resistResult.isEmpty() && emptyResist.isPresent() && !effect.getEffect().isBeneficial() && source == null) {
            ItemStack stack = emptyResist.get().stack();
            setPotionEffect(stack, effect, 4);
            cir.setReturnValue(false);
        }
    }
}