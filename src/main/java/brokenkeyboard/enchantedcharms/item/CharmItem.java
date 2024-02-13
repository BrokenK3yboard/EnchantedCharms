package brokenkeyboard.enchantedcharms.item;

import brokenkeyboard.enchantedcharms.enchantment.amethyst.AntidoteEnchantment;
import brokenkeyboard.enchantedcharms.enchantment.obsidian.FocusEnchantment;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class CharmItem extends Item implements ICurioItem {

    public CharmItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    public static Optional<SlotResult> getCurio(LivingEntity entity, Predicate<ItemStack> predicate) {
        if (entity == null) return Optional.empty();
        return CuriosApi.getCuriosHelper().findFirstCurio(entity, predicate);
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        if (FocusEnchantment.FOCUS_ENCH.test(stack)) {
            FocusEnchantment.setStacks(stack, 0);
        }
        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
        if (stack.getTag() != null && stack.getTag().contains("attribute")) {
            Stat stat = Stat.getStat(stack.getTag().getString("attribute"));
            if (stat == null) return multimap;
            multimap.put(stat.ATTRIBUTE, new AttributeModifier(uuid, stat.NAME, stat.AMOUNT, stat.OPERATION));
        }
        return multimap;
    }

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
        AntidoteEnchantment.getHoverText(tooltips, stack);
        FocusEnchantment.getHoverText(tooltips, stack);
        return tooltips;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return !stack.isEnchanted();
    }
}