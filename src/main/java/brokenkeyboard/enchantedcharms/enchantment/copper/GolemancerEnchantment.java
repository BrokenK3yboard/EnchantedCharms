package brokenkeyboard.enchantedcharms.enchantment.copper;

import brokenkeyboard.enchantedcharms.EnchantedCharms;
import brokenkeyboard.enchantedcharms.enchantment.CharmEnchantment;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import static brokenkeyboard.enchantedcharms.item.CharmItem.getCurio;

public class GolemancerEnchantment extends CharmEnchantment {

    public static final Predicate<ItemStack> GOLEM_ENCH = stack -> (EnchantmentHelper.getTagEnchantmentLevel(EnchantedCharms.GOLEMANCER.get(), stack) > 0);

    public GolemancerEnchantment(EnchantmentCategory category) {
        super(category);
        MinecraftForge.EVENT_BUS.addListener(this::spawnGolem);
    }

    public void spawnGolem(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof IronGolem || event.getEntity() instanceof SnowGolem)) return;
        List<LivingEntity> list = getNearbyEntities(event.getEntity().position(), event.getLevel(), 5);
        if (list.size() < 1) return;

        for (LivingEntity entity : list) {
            Optional<SlotResult> curio = getCurio(entity, GOLEM_ENCH);
            if (curio.isPresent()) {
                if (event.getEntity() instanceof IronGolem golem && golem.isPlayerCreated()) {
                    Objects.requireNonNull(golem.getAttribute(Attributes.ARMOR)).setBaseValue(golem.getArmorValue() + 12);
                } else if (event.getEntity() instanceof SnowGolem golem) {
                    Objects.requireNonNull(golem.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(golem.getHealth() + 6);
                    golem.heal(golem.getMaxHealth());
                    golem.getPersistentData().putInt("golemancer", 1);
                }
                return;
            }
        }
    }
}