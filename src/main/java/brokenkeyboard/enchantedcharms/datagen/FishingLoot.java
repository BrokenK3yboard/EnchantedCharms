package brokenkeyboard.enchantedcharms.datagen;

import brokenkeyboard.enchantedcharms.EnchantedCharms;
import brokenkeyboard.enchantedcharms.enchantment.copper.GrapplerEnchantment;
import brokenkeyboard.enchantedcharms.item.CharmItem;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FishingLoot extends LootModifier {

    public static final Supplier<Codec<FishingLoot>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, FishingLoot::new)));
    public static final Predicate<ItemStack> FISHING_ENCH = stack -> (EnchantmentHelper.getTagEnchantmentLevel(EnchantedCharms.ANGLERS_BOON.get(), stack) > 0);

    protected FishingLoot(LootItemCondition[] condition) {
        super(condition);
    }

    @NotNull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (!(context.getParamOrNull(LootContextParams.KILLER_ENTITY) instanceof Player player)) return generatedLoot;
        Optional<SlotResult> curio = CharmItem.getCurio(player, FISHING_ENCH);
        if (curio.isEmpty()) return generatedLoot;

        RandomSource random = player.getRandom();

        for (ItemStack stack : generatedLoot) {
            //if (random.nextDouble() > 0.4) continue;
            if (stack.getMaxStackSize() > 1) {
                stack.setCount(stack.getCount() * 2);
            } else if (stack.getMaxDamage() > 0) {
                int durability = (int) Math.floor((stack.getMaxDamage() - stack.getDamageValue()) * 1.5);
                int newDurability = (Math.max(stack.getMaxDamage() - durability, 0));
                stack.setDamageValue(newDurability);
            }
            player.addEffect(new MobEffectInstance(MobEffects.LUCK, 6000, 1, true, true));
        }

        Level level = player.getLevel();

        if (player.fishing != null && random.nextDouble() < 0.08 && level.getDifficulty().getId() > 0) {
            Guardian guardian = new Guardian(EntityType.GUARDIAN, player.getLevel());
            guardian.setPos(player.fishing.position());
            level.addFreshEntity(guardian);
            GrapplerEnchantment.pullEntity(player.position(), guardian);
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}