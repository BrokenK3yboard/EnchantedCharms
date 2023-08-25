package brokenkeyboard.enchantedcharms.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

import static brokenkeyboard.enchantedcharms.EnchantedCharms.ENHANCED_TNT;

public class PrimedTntEnhanced extends PrimedTnt {

    private LivingEntity owner;

    public PrimedTntEnhanced(EntityType<PrimedTntEnhanced> type, Level level) {
        super(type, level);
    }

    public PrimedTntEnhanced(Level level, Vec3 pos, LivingEntity owner) {
        this(ENHANCED_TNT.get(), level);
        this.setPos(pos.x(), pos.y(), pos.z());
        double d0 = level.random.nextDouble() * (double) ((float) Math.PI * 2F);
        this.setDeltaMovement(-Math.sin(d0) * 0.02D, 0.2F, -Math.cos(d0) * 0.02D);
        this.setFuse(80);
        this.xo = pos.x();
        this.yo = pos.y();
        this.zo = pos.z();
        this.owner = owner;
    }

    @Override
    protected void explode() {
        this.level().explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 8.0F, Level.ExplosionInteraction.TNT);
    }

    @Override
    @Nullable
    public LivingEntity getOwner() {
        return this.owner;
    }
}