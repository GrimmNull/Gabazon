package com.grimmyboi.javaengineering.data.client;

import com.grimmyboi.javaengineering.Main;
import com.grimmyboi.javaengineering.setup.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Main.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.THERMAL_GENERATOR.get());
        simpleBlock(ModBlocks.FIVE_G_ANTENNA.get());
        simpleBlock(ModBlocks.GABAZON_STATION.get());
        simpleBlock(ModBlocks.MONITOR.get());
        simpleBlock(ModBlocks.ENERGY_PIPE.get());
    }
}
