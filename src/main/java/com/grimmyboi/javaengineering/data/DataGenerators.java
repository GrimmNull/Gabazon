package com.grimmyboi.javaengineering.data;

import com.grimmyboi.javaengineering.Main;
import com.grimmyboi.javaengineering.data.client.ModBlockStateProvider;
import com.grimmyboi.javaengineering.data.client.ModItemModelProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.io.IOException;

@Mod.EventBusSubscriber(modid= Main.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    private DataGenerators(){

    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) throws IOException {
        DataGenerator gen=event.getGenerator();
        ExistingFileHelper help= event.getExistingFileHelper();

        //gen.addProvider(new ModBlockStateProvider(gen,help));
        //gen.addProvider(new ModItemModelProvider(gen, help));

        ModBlockTagsProvider blockTagsProvider=new ModBlockTagsProvider(gen,help);
        gen.addProvider(blockTagsProvider);
        gen.addProvider(new ModItemTagsProvider(gen,blockTagsProvider,help));

        gen.addProvider(new ModLootTableProvider(gen));
        gen.addProvider(new ModRecipeProvider(gen));
        System.out.println("The path: " + gen.getOutputFolder().toString());
        gen.run();
    }
}
