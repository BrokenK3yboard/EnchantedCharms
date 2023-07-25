package brokenkeyboard.enchantedcharms.mixin;

import brokenkeyboard.enchantedcharms.item.CharmItem;
import brokenkeyboard.enchantedcharms.item.Stat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin {
    @Inject(method = {"lambda$clickMenuButton$1(Lnet/minecraft/world/item/ItemStack;ILnet/minecraft/world/entity/player/Player;ILnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V",
            "m_39292_(Ljava/util/function/BiConsumer;)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;setChanged()V"))
    private void execute(ItemStack stack, int slot, Player player, int cost, ItemStack stack2, Level level, BlockPos blockPos, CallbackInfo ci) {
        if (!(level.isClientSide()) && stack.getItem() instanceof CharmItem) {
            EnchantmentMenu menu = ((EnchantmentMenu) (Object) this);
            if (player.getRandom().nextDouble() < menu.costs[slot] * 0.02) {
                stack.getOrCreateTag().putString("attribute", Stat.getRandomStat(player.getRandom()).name());
            }
        }
    }
}