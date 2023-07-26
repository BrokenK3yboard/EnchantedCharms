package brokenkeyboard.enchantedcharms.enchantment.copper;

import brokenkeyboard.enchantedcharms.EnchantedCharms;
import brokenkeyboard.enchantedcharms.enchantment.CharmEnchantment;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;
import java.util.function.Predicate;

import static brokenkeyboard.enchantedcharms.item.CharmItem.getCurio;

public class GrapplerEnchantment extends CharmEnchantment {

    public static final Predicate<ItemStack> GRAPPLING_ENCH = stack -> (EnchantmentHelper.getTagEnchantmentLevel(EnchantedCharms.GRAPPLER.get(), stack) > 0);

    public GrapplerEnchantment(EnchantmentCategory category) {
        super(category);
        MinecraftForge.EVENT_BUS.addListener(this::useGrapple);
    }

    public void useGrapple(EntityLeaveLevelEvent event) {
        if (!((event.getEntity() instanceof FishingHook hook) && (hook.getOwner() instanceof LivingEntity entity))) return;
        Optional<SlotResult> curio = getCurio(entity, GRAPPLING_ENCH);
        if (curio.isEmpty() || !(entity.getMainHandItem().getItem() instanceof FishingRodItem || entity.getOffhandItem().getItem() instanceof FishingRodItem)) return;

        if (hook.isOnGround()) {
            pullEntity(hook.position(), (LivingEntity) hook.getOwner());
        } else if (hook.getHookedIn() instanceof LivingEntity target) {
            pullEntity(hook.getOwner().position(), target);
        }
    }

    public static void pullEntity(Vec3 position, LivingEntity entity) {
        double distance = position.distanceTo(entity.position());
        double x = (1.0 + 0.07 * distance) * (position.x() - entity.getX()) / distance;
        double y = (1.0 + 0.03 * distance) * (position.y() - entity.getY()) / distance + 0.04 * distance;
        double z = (1.0 + 0.07 * distance) * (position.z() - entity.getZ()) / distance;
        entity.setDeltaMovement(new Vec3(x, y, z));
    }
}