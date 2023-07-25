package brokenkeyboard.enchantedcharms.enchantment.copper;

import brokenkeyboard.enchantedcharms.EnchantedCharms;
import brokenkeyboard.enchantedcharms.enchantment.CharmEnchantment;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;
import java.util.function.Predicate;

import static brokenkeyboard.enchantedcharms.item.CharmItem.getCurio;

public class RepairEnchantment extends CharmEnchantment {

    public static final Predicate<ItemStack> REPAIR_ENCH = stack -> (EnchantmentHelper.getItemEnchantmentLevel(EnchantedCharms.MASTERWORK.get(), stack) > 0);

    public RepairEnchantment(EnchantmentCategory category) {
        super(category);
        MinecraftForge.EVENT_BUS.addListener(this::repairItem);
    }

    public void repairItem(AnvilRepairEvent event) {
        LivingEntity entity = event.getPlayer();
        Optional<SlotResult> curio = getCurio(entity, REPAIR_ENCH);
        if (curio.isEmpty()) return;

        ItemStack input = event.getItemInput();
        ItemStack result = event.getItemResult();
        int bonusRepair = (int) (Math.floor((input.getDamageValue() - result.getDamageValue()) * 0.25));

        result.setDamageValue(result.getDamageValue() - bonusRepair);
        result.setRepairCost(input.getBaseRepairCost());
        event.setBreakChance(0.0F);
    }
}