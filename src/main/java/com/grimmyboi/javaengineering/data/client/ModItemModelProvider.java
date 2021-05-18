package com.grimmyboi.javaengineering.data.client;

import com.grimmyboi.javaengineering.Main;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels(){
        blockBuilder("thermal_generator");
        blockBuilder("five_g_antenna");
        blockBuilder("monitor");
        blockBuilder("gabazon_station");
        blockBuilder("energy_pipe");

        ModelFile itemGenerated= getExistingFile(mcLoc("item/generated"));

        itemBuilder(itemGenerated,"engineer_key");
        itemBuilder(itemGenerated,"wallet");
    }

    private ItemModelBuilder blockBuilder(String name){
        return withExistingParent(name, "block/" + name);
    }

    private ItemModelBuilder itemBuilder(ModelFile itemGenerated, String name){
        return getBuilder(name).parent(itemGenerated).texture("layer0","item/" +name);
    }
}
