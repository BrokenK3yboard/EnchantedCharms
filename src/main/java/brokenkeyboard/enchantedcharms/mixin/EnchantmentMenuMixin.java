package brokenkeyboard.enchantedcharms.mixin;

import brokenkeyboard.enchantedcharms.item.CharmItem;
import brokenkeyboard.enchantedcharms.item.Stat;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import top.theillusivec4.curios.api.SlotResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static brokenkeyboard.enchantedcharms.enchantment.emerald.RepositoryEnchantment.*;
import static brokenkeyboard.enchantedcharms.item.CharmItem.getCurio;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin {

    @Shadow @Final private RandomSource random;
    @Shadow @Final public int[] costs;

    @Inject(method = {"lambda$clickMenuButton$1(Lnet/minecraft/world/item/ItemStack;ILnet/minecraft/world/entity/player/Player;ILnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V",
            "m_39292_(Ljava/util/function/BiConsumer;)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;setChanged()V"))
    private void addCharmStat(ItemStack stack, int slot, Player player, int cost, ItemStack stack1, Level level, BlockPos blockPos, CallbackInfo ci) {
        if (!(level.isClientSide()) && stack.getItem() instanceof CharmItem) {
            EnchantmentMenu menu = ((EnchantmentMenu) (Object) this);
            if (player.getRandom().nextDouble() < menu.costs[slot] * 0.02) {
                stack.getOrCreateTag().putString("attribute", Stat.getRandomStat(player.getRandom()).name());
            }
        }
    }

    @Inject(method = {"lambda$clickMenuButton$1(Lnet/minecraft/world/item/ItemStack;ILnet/minecraft/world/entity/player/Player;ILnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V",
            "m_39292_(Ljava/util/function/BiConsumer;)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void modifyEnchantment(ItemStack stack, int slot, Player player, int cost, ItemStack stack1, Level level, BlockPos pos, CallbackInfo ci, ItemStack stack2, List<EnchantmentInstance> enchants) {
        final int xpLevel = this.costs[slot];
        final int xpCost = getXPFromLevels(xpLevel) - getXPFromLevels(xpLevel - 1);
        boolean canEnhance = true;
        Predicate<ItemStack> validCharm = charmStack -> EXP_ENCH.test(charmStack) && getLevels(getStoredXP(charmStack)) >= xpLevel;
        Optional<SlotResult> curio = getCurio(player, validCharm);

        if (curio.isPresent()) {
            ItemStack charm = curio.get().stack();
            List<EnchantmentInstance> extraEnchants = EnchantmentHelper.selectEnchantment(this.random, stack, xpLevel, false);

            ArrayList<Integer> upgradeIndex = new ArrayList<>();

            for (EnchantmentInstance inst : enchants) {
                EnchantmentHelper.filterCompatibleEnchantments(extraEnchants, inst);
                if (inst.enchantment.getMaxLevel() > 1) {
                    upgradeIndex.add(enchants.indexOf(inst));
                }
            }

            if (extraEnchants.isEmpty() && !upgradeIndex.isEmpty()) {
                int index = upgradeIndex.size() > 1 ? upgradeIndex.get(this.random.nextInt(upgradeIndex.size())) : 0;
                EnchantmentInstance upgrEnch = new EnchantmentInstance(enchants.get(index).enchantment, enchants.get(index).level + 1);
                enchants.set(index, upgrEnch);
            } else if (!extraEnchants.isEmpty()) {
                int index = extraEnchants.size() == 1 ? 0 : random.nextInt(extraEnchants.size());
                enchants.add(extraEnchants.get(index));
            } else {
                canEnhance = false;
            }
            charm.getOrCreateTag().putInt("xp", getStoredXP(charm) - (canEnhance ? xpCost : 0));
        }
    }
}