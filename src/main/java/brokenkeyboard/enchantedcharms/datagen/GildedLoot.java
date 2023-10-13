package brokenkeyboard.enchantedcharms.datagen;

import brokenkeyboard.enchantedcharms.EnchantedCharms;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotResult;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static brokenkeyboard.enchantedcharms.item.CharmItem.getCurio;

public class GildedLoot extends LootModifier {

    public static final Supplier<Codec<GildedLoot>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, GildedLoot::new)));
    public static final Predicate<ItemStack> GILDED_ENCH = stack -> (EnchantmentHelper.getTagEnchantmentLevel(EnchantedCharms.GILDED.get(), stack) > 0);
    public final HashMap<Item, Item> gildedDrops = new HashMap<>();

    public GildedLoot(LootItemCondition[] condition) {
        super(condition);
        gildedDrops.put(Items.APPLE, Items.GOLDEN_APPLE);
        gildedDrops.put(Items.CARROT, Items.GOLDEN_CARROT);
        gildedDrops.put(Items.MELON_SLICE, Items.GLISTERING_MELON_SLICE);
    }

    @NotNull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        Player player = findPlayer(context);
        Optional<SlotResult> curio = getCurio(player, GILDED_ENCH);
        if (player == null || curio.isEmpty()) return generatedLoot;

        ObjectArrayList<ItemStack> modifiedLoot = new ObjectArrayList<>();
        RandomSource random = player.getRandom();

        for (ItemStack stack : generatedLoot) {
            Item item = stack.getItem();
            if (gildedDrops.containsKey(item) && random.nextDouble() < 1) {
                stack.setCount(stack.getCount() - 1);
                modifiedLoot.add(new ItemStack(gildedDrops.get(item)));
            } else if (stack.is(Tags.Items.RAW_MATERIALS) && random.nextDouble() < 1) {
                modifiedLoot.add(new ItemStack(Items.RAW_GOLD));
            }
            modifiedLoot.add(stack);
        }
        return modifiedLoot;
    }

    @Nullable
    public static Player findPlayer(LootContext context) {
        if (context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof Player player) return player;
        if (context.getParamOrNull(LootContextParams.DIRECT_KILLER_ENTITY) instanceof Player player) return player;
        if (context.getParamOrNull(LootContextParams.KILLER_ENTITY) instanceof Player player) return player;
        if (context.getParamOrNull(LootContextParams.LAST_DAMAGE_PLAYER) != null) return context.getParamOrNull(LootContextParams.LAST_DAMAGE_PLAYER);
        return null;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}