package com.grimmyboi.javaengineering.data;

import com.grimmyboi.javaengineering.Main;
import com.grimmyboi.javaengineering.setup.ModBlocks;
import com.grimmyboi.javaengineering.setup.ModTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;


public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(DataGenerator p_i48256_1_, ExistingFileHelper existingFileHelper) {
        super(p_i48256_1_, Main.MOD_ID, existingFileHelper);
    }
    @Override
    protected void addTags(){
        tag(ModTags.Blocks.REDSTONE_ANTENNA).add(ModBlocks.FIVE_G_ANTENNA.get());
        tag(ModTags.Blocks.REDSTONE_GENERATOR).add(ModBlocks.THERMAL_GENERATOR.get());
        tag(ModTags.Blocks.REDSTONE_MONITOR).add(ModBlocks.MONITOR.get());
        tag(ModTags.Blocks.REDSTONE_PIPE).add(ModBlocks.ENERGY_PIPE.get());
        tag(ModTags.Blocks.REDSTONE_STATION).add(ModBlocks.GABAZON_STATION.get());


    }
}
