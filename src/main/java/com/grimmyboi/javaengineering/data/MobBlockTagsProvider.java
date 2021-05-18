package com.grimmyboi.javaengineering.data;

import com.grimmyboi.javaengineering.Main;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class MobBlockTagsProvider extends BlockTagsProvider {

    public MobBlockTagsProvider(DataGenerator p_i48256_1_,ExistingFileHelper existingFileHelper) {
        super(p_i48256_1_, Main.MOD_ID, existingFileHelper);
    }
    //@Override
    protected void registerTags(){
        //getOrCreateRawBuilder()
    }
}
