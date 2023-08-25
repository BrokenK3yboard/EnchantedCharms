package brokenkeyboard.enchantedcharms.enchantment.amethyst;

import brokenkeyboard.enchantedcharms.EnchantedCharms;
import brokenkeyboard.enchantedcharms.enchantment.CharmEnchantment;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;
import java.util.function.Predicate;

import static brokenkeyboard.enchantedcharms.item.CharmItem.getCurio;

public class ClusterBrewEnchantment extends CharmEnchantment {

    public static final Predicate<ItemStack> PROXY_ENCH = stack -> (EnchantmentHelper.getTagEnchantmentLevel(EnchantedCharms.CLUSTER_BREW.get(), stack) > 0);

    public ClusterBrewEnchantment(EnchantmentCategory category) {
        super(category);
        MinecraftForge.EVENT_BUS.addListener(this::splitPotions);
    }

    public void splitPotions(ProjectileImpactEvent event) {
        if (!(event.getProjectile() instanceof ThrownPotion potion) || !(potion.getOwner() instanceof LivingEntity owner) || (potion.getTags().contains("copy"))) return;
        Optional<SlotResult> curio = getCurio(owner, PROXY_ENCH);
        if (curio.isEmpty()) return;

        for (int i = 0; i < 2; i++) {
            RandomSource random = owner.getRandom();
            Level level = potion.level();
            ThrownPotion potion1 = new ThrownPotion(level, owner);

            potion1.setItem(potion.getItem());
            potion1.setPos(potion.position());
            potion1.setDeltaMovement(new Vec3(0.3 - random.nextInt(6) * 0.1, random.nextInt(3) * 0.1 + 0.1, 0.3 - random.nextInt(6) * 0.1));
            potion1.addTag("copy");
            level.addFreshEntity(potion1);
        }
    }
}