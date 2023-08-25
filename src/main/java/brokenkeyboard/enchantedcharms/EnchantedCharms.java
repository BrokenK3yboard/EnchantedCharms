package brokenkeyboard.enchantedcharms;

import brokenkeyboard.enchantedcharms.datagen.FishingLoot;
import brokenkeyboard.enchantedcharms.datagen.GildedLoot;
import brokenkeyboard.enchantedcharms.enchantment.CommonEvents;
import brokenkeyboard.enchantedcharms.enchantment.amethyst.AntidoteEnchantment;
import brokenkeyboard.enchantedcharms.enchantment.amethyst.ClusterBrewEnchantment;
import brokenkeyboard.enchantedcharms.enchantment.amethyst.PotencyEnchantment;
import brokenkeyboard.enchantedcharms.enchantment.amethyst.SurvivalistEnchantment;
import brokenkeyboard.enchantedcharms.enchantment.copper.GolemancerEnchantment;
import brokenkeyboard.enchantedcharms.enchantment.copper.GrapplerEnchantment;
import brokenkeyboard.enchantedcharms.enchantment.copper.OrdnanceEnchantment;
import brokenkeyboard.enchantedcharms.enchantment.copper.RepairEnchantment;
import brokenkeyboard.enchantedcharms.enchantment.emerald.AnglersBoonEnchantment;
import brokenkeyboard.enchantedcharms.enchantment.emerald.GildedEnchantment;
import brokenkeyboard.enchantedcharms.enchantment.emerald.ProspectingEnchantment;
import brokenkeyboard.enchantedcharms.enchantment.emerald.RepositoryEnchantment;
import brokenkeyboard.enchantedcharms.enchantment.obsidian.BulwarkEnchantment;
import brokenkeyboard.enchantedcharms.enchantment.obsidian.DefusingEnchantment;
import brokenkeyboard.enchantedcharms.enchantment.obsidian.FocusEnchantment;
import brokenkeyboard.enchantedcharms.enchantment.obsidian.HuntersMarkEnchantment;
import brokenkeyboard.enchantedcharms.entity.PrimedTntEnhanced;
import brokenkeyboard.enchantedcharms.item.CharmItem;
import com.mojang.serialization.Codec;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.curios.api.CuriosApi;

import static brokenkeyboard.enchantedcharms.EnchantedCharms.MOD_ID;

@SuppressWarnings("unused")
@Mod(MOD_ID)
public class EnchantedCharms {
    public static final String MOD_ID = "enchantedcharms";
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MOD_ID);
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLM = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MOD_ID);

    public static final TagKey<Item> CHARM = TagKey.create(Registries.ITEM, new ResourceLocation(CuriosApi.MODID, "charm"));

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> FISHING_LOOT = GLM.register("fishing_loot", FishingLoot.CODEC);
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> GILDED_LOOT = GLM.register("gilded_loot", GildedLoot.CODEC);

    public static final RegistryObject<Item> COPPER_CHARM = ITEMS.register("copper_charm", () -> new CharmItem(new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_CHARM = ITEMS.register("obsidian_charm", () -> new CharmItem(new Item.Properties()));
    public static final RegistryObject<Item> AMETHYST_CHARM = ITEMS.register("amethyst_charm", () -> new CharmItem(new Item.Properties()));
    public static final RegistryObject<Item> EMERALD_CHARM = ITEMS.register("emerald_charm", () -> new CharmItem(new Item.Properties()));

    public static final EnchantmentCategory CHARM_COPPER = EnchantmentCategory.create("CHARM_COPPER", item -> item == COPPER_CHARM.get());
    public static final EnchantmentCategory CHARM_OBSIDIAN = EnchantmentCategory.create("CHARM_OBSIDIAN", item -> item == OBSIDIAN_CHARM.get());
    public static final EnchantmentCategory CHARM_AMETHYST = EnchantmentCategory.create("CHARM_AMETHYST", item -> item == AMETHYST_CHARM.get());
    public static final EnchantmentCategory CHARM_EMERALD = EnchantmentCategory.create("CHARM_EMERALD", item -> item == EMERALD_CHARM.get());

    public static final RegistryObject<Enchantment> MASTERWORK = ENCHANTMENTS.register("masterwork", () -> new RepairEnchantment(CHARM_COPPER));
    public static final RegistryObject<Enchantment> GRAPPLER = ENCHANTMENTS.register("grappler", () -> new GrapplerEnchantment(CHARM_COPPER));
    public static final RegistryObject<Enchantment> GOLEMANCER = ENCHANTMENTS.register("golemancer", () -> new GolemancerEnchantment(CHARM_COPPER));
    public static final RegistryObject<Enchantment> ORDNANCE = ENCHANTMENTS.register("ordnance", () -> new OrdnanceEnchantment(CHARM_COPPER));

    public static final RegistryObject<Enchantment> DEFUSING = ENCHANTMENTS.register("defusing", () -> new DefusingEnchantment(CHARM_OBSIDIAN));
    public static final RegistryObject<Enchantment> BULWARK = ENCHANTMENTS.register("bulwark", () -> new BulwarkEnchantment(CHARM_OBSIDIAN));
    public static final RegistryObject<Enchantment> HUNTERS_MARK = ENCHANTMENTS.register("hunters_mark", () -> new HuntersMarkEnchantment(CHARM_OBSIDIAN));
    public static final RegistryObject<Enchantment> FOCUS = ENCHANTMENTS.register("focus", () -> new FocusEnchantment(CHARM_OBSIDIAN));

    public static final RegistryObject<Enchantment> SURVIVALIST = ENCHANTMENTS.register("survivalist", () -> new SurvivalistEnchantment(CHARM_AMETHYST));
    public static final RegistryObject<Enchantment> POTENCY = ENCHANTMENTS.register("potency", () -> new PotencyEnchantment(CHARM_AMETHYST));
    public static final RegistryObject<Enchantment> ANTIDOTE = ENCHANTMENTS.register("antidote", () -> new AntidoteEnchantment(CHARM_AMETHYST));
    public static final RegistryObject<Enchantment> CLUSTER_BREW = ENCHANTMENTS.register("cluster_brew", () -> new ClusterBrewEnchantment(CHARM_AMETHYST));

    public static final RegistryObject<Enchantment> REPOSITORY = ENCHANTMENTS.register("repository", () -> new RepositoryEnchantment(CHARM_EMERALD));
    public static final RegistryObject<Enchantment> PROSPECTING = ENCHANTMENTS.register("prospecting", () -> new ProspectingEnchantment(CHARM_EMERALD));
    public static final RegistryObject<Enchantment> ANGLERS_BOON = ENCHANTMENTS.register("anglers_boon", () -> new AnglersBoonEnchantment(CHARM_EMERALD));
    public static final RegistryObject<Enchantment> GILDED = ENCHANTMENTS.register("gilded", () -> new GildedEnchantment(CHARM_EMERALD));

    public static final RegistryObject<CreativeModeTab> CHARMS = CREATIVE_MODE_TABS.register(MOD_ID, () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .title(Component.translatable("itemGroup.enchantedcharms"))
            .icon(() -> new ItemStack(COPPER_CHARM.get()))
            .displayItems((parameters, output) -> {
                output.accept(COPPER_CHARM.get());
                output.accept(OBSIDIAN_CHARM.get());
                output.accept(AMETHYST_CHARM.get());
                output.accept(EMERALD_CHARM.get());
            }).build());

    public static final RegistryObject<EntityType<PrimedTntEnhanced>> ENHANCED_TNT = ENTITIES.register("enhanced_tnt", () ->
            EntityType.Builder.<PrimedTntEnhanced>of(PrimedTntEnhanced::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(0.98F, 0.98F)
                    .clientTrackingRange(10)
                    .updateInterval(10)
                    .build("enhanced_tnt"));

    public EnchantedCharms() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
        ENCHANTMENTS.register(bus);
        ENTITIES.register(bus);
        EFFECTS.register(bus);
        CREATIVE_MODE_TABS.register(bus);
        GLM.register(bus);
        MinecraftForge.EVENT_BUS.register(CommonEvents.class);
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void registerRenders(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ENHANCED_TNT.get(), TntRenderer::new);
        }
    }
}