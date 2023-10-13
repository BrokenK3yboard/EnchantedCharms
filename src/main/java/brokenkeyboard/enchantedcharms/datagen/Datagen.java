package brokenkeyboard.enchantedcharms.datagen;

import brokenkeyboard.enchantedcharms.EnchantedCharms;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeBlockTagsProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = EnchantedCharms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Datagen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        ForgeBlockTagsProvider blockTagProvider = new ForgeBlockTagsProvider(generator, fileHelper);

        if (event.includeServer()) {
            generator.addProvider(new ModTags(generator, blockTagProvider, fileHelper));
            generator.addProvider(new Recipes(generator));
            generator.addProvider(new GLMProvider(generator));
        }
    }
}