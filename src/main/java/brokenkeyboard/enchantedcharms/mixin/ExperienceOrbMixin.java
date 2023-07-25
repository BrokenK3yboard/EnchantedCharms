package brokenkeyboard.enchantedcharms.mixin;

import brokenkeyboard.enchantedcharms.enchantment.copper.RepairEnchantment;
import brokenkeyboard.enchantedcharms.item.CharmItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Map;
import java.util.Optional;

@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbMixin {

    @Inject(method = "repairPlayerItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setDamageValue(I)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void repairPlayerItems(Player player, int value, CallbackInfoReturnable<Integer> cir, Map.Entry<EquipmentSlot, ItemStack> entry, ItemStack stack) {
        Optional<SlotResult> curio = CharmItem.getCurio(player, RepairEnchantment.REPAIR_ENCH);
        if (curio.isPresent()) {
            ExperienceOrb orb = (ExperienceOrb) (Object) this;
            int repair = (int) Math.min(Math.ceil((double) orb.getValue() / 2), stack.getDamageValue());
            stack.setDamageValue(stack.getDamageValue() - repair);
        }
    }
}