package brokenkeyboard.enchantedcharms.mixin;

import brokenkeyboard.enchantedcharms.datagen.GildedLoot;
import brokenkeyboard.enchantedcharms.item.CharmItem;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
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

    @Shadow
    public abstract Item getItem();

    @Inject(method = "hurtEnemy", at = @At("HEAD"), cancellable = true)
    public void hurtEnemy(LivingEntity entity, Player player, CallbackInfo ci) {
        Optional<SlotResult> curio = CharmItem.getCurio(player, GildedLoot.GILDED_ENCH);
        if (curio.isPresent()) {
            if (this.getItem() instanceof TieredItem tieredItem && tieredItem.getTier() == Tiers.GOLD && entity.getHealth() <= tieredItem.getTier().getAttackDamageBonus()) {
                player.awardStat(Stats.ITEM_USED.get(this.getItem()));
                ci.cancel();
            }
        }
    }

    @Inject(method = "mineBlock", at = @At("HEAD"), cancellable = true)
    private void mineBlock(Level level, BlockState state, BlockPos pos, Player player, CallbackInfo ci) {
        Optional<SlotResult> curio = CharmItem.getCurio(player, GildedLoot.GILDED_ENCH);
        if (curio.isPresent()) {
            if (this.getItem() instanceof DiggerItem diggerItem && diggerItem.getTier() == Tiers.GOLD
                    && (state.is(Tags.Blocks.STONE) || state.is(Tags.Blocks.SAND) || state.is(Tags.Blocks.GRAVEL) && state.is(Tags.Blocks.NETHERRACK)
                    || state.getMaterial() == Material.DIRT)) {
                player.awardStat(Stats.ITEM_USED.get(this.getItem()));
                ci.cancel();
            }
        }
    }
}