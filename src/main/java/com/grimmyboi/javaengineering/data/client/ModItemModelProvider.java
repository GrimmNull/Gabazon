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
        withExistingParent("thermal_generator",modLoc("block/thermal_generator"));

        ModelFile itemGenerated= getExistingFile(mcLoc("item/generated"));

        builder(itemGenerated,"engineer_key");
    }

    private ItemModelBuilder builder(ModelFile itemGenerated, String name){
        return getBuilder(name).parent(itemGenerated).texture("layer0","item/" +name);
    }
}
