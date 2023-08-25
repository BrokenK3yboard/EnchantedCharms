package brokenkeyboard.enchantedcharms.datagen;

import brokenkeyboard.enchantedcharms.EnchantedCharms;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = EnchantedCharms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Datagen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        BlockTags blockTags = new BlockTags(output, provider, fileHelper);

        if (event.includeServer()) {
            generator.addProvider(true, blockTags);
            generator.addProvider(true, new ItemTags(output, provider, blockTags.contentsGetter(), fileHelper));
            generator.addProvider(true, new Recipes(output));
        }
    }
}
