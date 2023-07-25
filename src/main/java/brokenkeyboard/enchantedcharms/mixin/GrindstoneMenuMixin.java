package brokenkeyboard.enchantedcharms.mixin;

import brokenkeyboard.enchantedcharms.item.CharmItem;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GrindstoneMenu.class)
public abstract class GrindstoneMenuMixin {

    @Inject(method = "removeNonCurses", at = @At(value = "RETURN"))
    private void removeNonCurses(ItemStack stack, int durability, int count, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack result = cir.getReturnValue();
        if (result.getItem() instanceof CharmItem) {
            result.removeTagKey("attribute");
            result.removeTagKey("stacks");
            result.removeTagKey("potion_effect");
            result.removeTagKey("uses");
        }
    }
}