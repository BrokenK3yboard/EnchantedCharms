package brokenkeyboard.enchantedcharms.enchantment.obsidian;

import brokenkeyboard.enchantedcharms.EnchantedCharms;
import brokenkeyboard.enchantedcharms.enchantment.CharmEnchantment;
import brokenkeyboard.enchantedcharms.item.CharmItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ExplosionEvent;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

public class DefusingEnchantment extends CharmEnchantment {

    public static final Predicate<ItemStack> DEFUSING_ENCH = stack -> (EnchantmentHelper.getItemEnchantmentLevel(EnchantedCharms.DEFUSING.get(), stack) > 0);
    public static final int RANGE = 5;

    public DefusingEnchantment(EnchantmentCategory category) {
        super(category);
        MinecraftForge.EVENT_BUS.addListener(this::defuseExplosion);
    }

    public void defuseExplosion(ExplosionEvent.Start event) {
        Explosion explosion = event.getExplosion();
        Vec3 position = explosion.getPosition();
        Level level = event.getWorld();
        List<LivingEntity> list = getNearbyEntities(position, level, RANGE);
        if (list.size() < 1) return;

        for (LivingEntity entity : list) {
            Optional<SlotResult> curio = CharmItem.getCurio(entity, DEFUSING_ENCH);
            double distance = position.distanceTo(entity.position());
            double chance = Math.min(((RANGE - distance) / RANGE + 0.3), 1);
            Random random = entity.getRandom();

            if (curio.isPresent() && random.nextDouble() < chance) {
                event.setCanceled(true);
                defuseParticles(level, position, random);
                return;
            }
        }
    }

    public void defuseParticles(Level level, Vec3 position, Random random) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        for(int i = 0; i < 20; ++i) {
            double d0 = position.x() + random.nextGaussian() * 0.02D;
            double d1 = position.y() + 0.5 + random.nextGaussian() * 0.02D;
            double d2 = position.z() + random.nextGaussian() * 0.02D;
            serverLevel.sendParticles(ParticleTypes.POOF, d0, d1, d2, 1, 0, 0, 0, 0.1);
        }
    }
}