package brokenkeyboard.enchantedcharms.enchantment.amethyst;

import brokenkeyboard.enchantedcharms.EnchantedCharms;
import brokenkeyboard.enchantedcharms.enchantment.CharmEnchantment;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.Event;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static brokenkeyboard.enchantedcharms.item.CharmItem.getCurio;

public class AntidoteEnchantment extends CharmEnchantment {

    public static final Predicate<ItemStack> ANTIDOTE_ENCH = stack -> (EnchantmentHelper.getItemEnchantmentLevel(EnchantedCharms.ANTIDOTE.get(), stack) > 0);
    public static final Predicate<ItemStack> EMPTY_RESIST = stack -> ANTIDOTE_ENCH.test(stack) && getPotionEffect(stack) == null;

    public AntidoteEnchantment(EnchantmentCategory category) {
        super(category);
        MinecraftForge.EVENT_BUS.addListener(this::absorbEffect);
    }

    public void absorbEffect(PotionEvent.PotionApplicableEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (event.getPotionEffect().getEffect().isBeneficial() || getCurio(entity, ANTIDOTE_ENCH).isEmpty()) return;

        Predicate<ItemStack> hasResist = stack -> getPotionEffect(stack) != null && getPotionEffect(stack).getEffect() == event.getPotionEffect().getEffect();
        Optional<SlotResult> resistResult = getCurio(entity, hasResist);

        if (resistResult.isPresent()) {
            ItemStack stack = resistResult.get().stack();
            setUses(stack, getUses(stack) - 1);
            event.setResult(Event.Result.DENY);
        }
    }

    public static void setPotionEffect(ItemStack stack, MobEffectInstance effect, int uses) {
        CompoundTag potionTag = new CompoundTag();
        effect.save(potionTag);
        stack.addTagElement("potion_effect", potionTag);
        setUses(stack, uses);
    }

    public static MobEffectInstance getPotionEffect(ItemStack stack) {
        CompoundTag tag = stack.getTagElement("potion_effect");
        return tag == null ? null : MobEffectInstance.load(tag);
    }

    public static void setUses(ItemStack stack, int uses) {
        stack.getOrCreateTag().putInt("uses", uses);
        if (uses <= 0) {
            stack.removeTagKey("potion_effect");
        }
    }

    public static int getUses(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag == null ? 0 : tag.getInt("uses");
    }

    public static void getHoverText(List<Component> components, ItemStack stack) {
        if (ANTIDOTE_ENCH.test(stack) && getPotionEffect(stack) != null) {
            components.add(new TextComponent(getPotionEffect(stack).getEffect().getDisplayName().getString() + " ")
                    .append(new TranslatableComponent("enchantedcharms.immunity"))
                    .append(new TextComponent(" (" + getUses(stack) + ")"))
                    .withStyle(ChatFormatting.BLUE));
        }
    }
}