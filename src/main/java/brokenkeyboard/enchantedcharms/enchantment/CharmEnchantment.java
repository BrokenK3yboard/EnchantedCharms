package brokenkeyboard.enchantedcharms.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class CharmEnchantment extends Enchantment {
    protected CharmEnchantment(EnchantmentCategory category) {
        super(Rarity.COMMON, category, EquipmentSlot.values());
    }

    @Override
    protected boolean checkCompatibility(@NotNull Enchantment enchantment) {
        return super.checkCompatibility(enchantment) && !(enchantment instanceof CharmEnchantment);
    }

    @Override
    public boolean isAllowedOnBooks() {
        return false;
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    public int getMinCost(int level) {
        return 1;
    }

    public int getMaxCost(int level) {
        return 50;
    }

    public static List<LivingEntity> getNearbyEntities(Vec3 pos, Level level, int range) {
        AABB aabb = new AABB(pos.x() - range, pos.y() - range, pos.z() - range, pos.x() + range, pos.y() + range, pos.z() + range);
        return level.getEntitiesOfClass(LivingEntity.class, aabb);
    }
}