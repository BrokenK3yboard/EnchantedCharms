package brokenkeyboard.enchantedcharms.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.animal.SnowGolem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnowGolem.class)
public abstract class SnowGolemMixin {

    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/SnowGolem;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean preventMelting(SnowGolem instance, DamageSource damageSource, float v) {
        SnowGolem golem = ((SnowGolem) (Object) this);
        if (!(golem.getPersistentData().contains("golemancer")))
            golem.hurt(golem.damageSources().onFire(), 1.0F);
        return false;
    }

    @Inject(method = "isSensitiveToWater", at = @At(value = "RETURN"), cancellable = true)
    private void preventWaterDamage(CallbackInfoReturnable<Boolean> cir) {
        SnowGolem golem = ((SnowGolem) (Object) this);
        if (golem.getPersistentData().contains("golemancer"))
            cir.setReturnValue(false);
    }
}
