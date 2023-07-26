package brokenkeyboard.enchantedcharms.item;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Stat {

    MAX_HEALTH(Attributes.MAX_HEALTH, "Health", 2.0, AttributeModifier.Operation.ADDITION),
    KNOCKBACK_RESISTANCE(Attributes.KNOCKBACK_RESISTANCE, "Armor knockback resistance", 0.1, AttributeModifier.Operation.ADDITION),
    MOVEMENT_SPEED(Attributes.MOVEMENT_SPEED, "Movement speed", 0.05, AttributeModifier.Operation.MULTIPLY_BASE),
    ATTACK_SPEED(Attributes.ATTACK_SPEED, "Attack speed", 0.05, AttributeModifier.Operation.MULTIPLY_BASE),
    ARMOR_TOUGHNESS(Attributes.ARMOR_TOUGHNESS, "Armor toughness", 1, AttributeModifier.Operation.ADDITION),
    LUCK(Attributes.LUCK, "Luck", 1, AttributeModifier.Operation.ADDITION);

    public final Attribute ATTRIBUTE;
    public final String NAME;
    public final double AMOUNT;
    public final AttributeModifier.Operation OPERATION;

    final static Map<String, Stat> enums = new HashMap<>();

    Stat(Attribute attribute, String name, double amount, AttributeModifier.Operation operation) {
        this.ATTRIBUTE = attribute;
        this.NAME = name;
        this.AMOUNT = amount;
        this.OPERATION = operation;
    }

    static {
        for (Stat stat : EnumSet.allOf(Stat.class)) {
            enums.put(stat.name(), stat);
        }
    }

    public static Stat getRandomStat(RandomSource random) {
        return Stat.values()[random.nextInt(enums.size())];
    }

    public static Stat getStat(String stat) {
        if (enums.containsKey(stat)) return enums.get(stat);
        return null;
    }
}