package brokenkeyboard.enchantedcharms.datagen;

import brokenkeyboard.enchantedcharms.EnchantedCharms;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotResult;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

import static brokenkeyboard.enchantedcharms.item.CharmItem.getCurio;

public class GildedLoot extends LootModifier {

    public static final Predicate<ItemStack> GILDED_ENCH = stack -> (EnchantmentHelper.getItemEnchantmentLevel(EnchantedCharms.GILDED.get(), stack) > 0);
    public final HashMap<Item, Item> gildedDrops = new HashMap<>();

    public GildedLoot(LootItemCondition[] condition) {
        super(condition);
        gildedDrops.put(Items.APPLE, Items.GOLDEN_APPLE);
        gildedDrops.put(Items.CARROT, Items.GOLDEN_CARROT);
        gildedDrops.put(Items.MELON_SLICE, Items.GLISTERING_MELON_SLICE);
    }

    @NotNull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        Player player = findPlayer(context);
        Optional<SlotResult> curio = getCurio(player, GILDED_ENCH);
        if (player == null || curio.isEmpty()) return generatedLoot;

        List<ItemStack> modifiedLoot = new ArrayList<>();
        Random random = player.getRandom();

        for (ItemStack stack : generatedLoot) {
            Item item = stack.getItem();
            if (gildedDrops.containsKey(item) && random.nextDouble() < 0.05) {
                stack.setCount(stack.getCount() - 1);
                modifiedLoot.add(new ItemStack(gildedDrops.get(item)));
            } else if (stack.is(Tags.Items.RAW_MATERIALS) && random.nextDouble() < 0.15) {
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

    public static class GildedLootSerializer extends GlobalLootModifierSerializer<GildedLoot> {

        @Override
        public GildedLoot read(ResourceLocation location, JsonObject object, LootItemCondition[] condition) {
            return new GildedLoot(condition);
        }

        @Override
        public JsonObject write(GildedLoot instance) {
            return this.makeConditions(instance.conditions);
        }
    }
}