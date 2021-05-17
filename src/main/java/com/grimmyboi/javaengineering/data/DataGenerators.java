package com.grimmyboi.javaengineering.data;

import com.grimmyboi.javaengineering.Main;
import com.grimmyboi.javaengineering.data.client.ModBlockStateProvider;
import com.grimmyboi.javaengineering.data.client.ModItemModelProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid= Main.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    private DataGenerators(){

    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator gen=event.getGenerator();
        ExistingFileHelper help= event.getExistingFileHelper();
        gen.addProvider(new ModBlockStateProvider(gen,help));
        gen.addProvider(new ModItemModelProvider(gen, help));
    }
}
