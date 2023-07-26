package brokenkeyboard.enchantedcharms.enchantment.copper;

import brokenkeyboard.enchantedcharms.EnchantedCharms;
import brokenkeyboard.enchantedcharms.enchantment.CharmEnchantment;
import brokenkeyboard.enchantedcharms.entity.PrimedTntEnhanced;
import brokenkeyboard.enchantedcharms.item.CharmItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;
import java.util.function.Predicate;

public class OrdnanceEnchantment extends CharmEnchantment {

    public static final Predicate<ItemStack> ORDNANCE_ENCH = stack -> (EnchantmentHelper.getTagEnchantmentLevel(EnchantedCharms.ORDNANCE.get(), stack) > 0);

    public OrdnanceEnchantment(EnchantmentCategory category) {
        super(category);
        MinecraftForge.EVENT_BUS.addListener(this::modifyTNT);
    }

    public void modifyTNT(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof PrimedTnt tnt) || tnt.getType() != EntityType.TNT || tnt.getOwner() == null) return;
        LivingEntity entity = tnt.getOwner();
        Optional<SlotResult> curio = CharmItem.getCurio(entity, ORDNANCE_ENCH);
        if (curio.isEmpty()) return;

        event.setCanceled(true);
        event.getLevel().addFreshEntity(new PrimedTntEnhanced(event.getLevel(), tnt.position(), entity));
    }
}