package brokenkeyboard.enchantedcharms.mixin;

import brokenkeyboard.enchantedcharms.enchantment.copper.GildedEnchantment;
import brokenkeyboard.enchantedcharms.item.CharmItem;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "hurtEnemy", at = @At("HEAD"), cancellable = true)
    public void hurtEnemy(LivingEntity entity, Player player, CallbackInfo ci) {
        Optional<SlotResult> curio = CharmItem.getCurio(player, GildedEnchantment.GILDED_ENCH);
        if (curio.isPresent()) {
            if (((ItemStack)(Object) this).getItem() instanceof TieredItem tieredItem && tieredItem.getTier() == Tiers.GOLD && entity.getHealth() <= tieredItem.getTier().getAttackDamageBonus()) {
                player.awardStat(Stats.ITEM_USED.get(((ItemStack)(Object) this).getItem()));
                ci.cancel();
            }
        }
    }

    @Inject(method = "mineBlock", at = @At("HEAD"), cancellable = true)
    private void mineBlock(Level level, BlockState state, BlockPos pos, Player player, CallbackInfo ci) {
        Optional<SlotResult> curio = CharmItem.getCurio(player, GildedEnchantment.GILDED_ENCH);
        if (curio.isPresent()) {
            if (((ItemStack)(Object) this).getItem() instanceof DiggerItem diggerItem && diggerItem.getTier() == Tiers.GOLD
                    && (state.is(Tags.Blocks.STONE) || state.is(Tags.Blocks.SAND) || state.is(Tags.Blocks.GRAVEL) && state.is(Tags.Blocks.NETHERRACK) ||
                    state.is(BlockTags.DIRT))) {
                player.awardStat(Stats.ITEM_USED.get(((ItemStack)(Object) this).getItem()));
                ci.cancel();
            }
        }
    }
}