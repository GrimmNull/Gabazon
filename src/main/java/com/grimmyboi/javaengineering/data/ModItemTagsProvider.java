package com.grimmyboi.javaengineering.data;

import com.grimmyboi.javaengineering.Main;
import com.grimmyboi.javaengineering.setup.ModItems;
import com.grimmyboi.javaengineering.setup.ModTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;


public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(DataGenerator p_i232552_1_, BlockTagsProvider p_i232552_2_, ExistingFileHelper existingFileHelper) {
        super(p_i232552_1_, p_i232552_2_, Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        copy(ModTags.Blocks.REDSTONE_ANTENNA, ModTags.Items.REDSTONE_ANTENNA);
        copy(ModTags.Blocks.REDSTONE_GENERATOR, ModTags.Items.REDSTONE_GENERATOR);
        copy(ModTags.Blocks.REDSTONE_MONITOR, ModTags.Items.REDSTONE_MONITOR);
        copy(ModTags.Blocks.REDSTONE_PIPE, ModTags.Items.REDSTONE_PIPE);
        copy(ModTags.Blocks.REDSTONE_STATION, ModTags.Items.REDSTONE_STATION);

        tag(ModTags.Items.TOOL_WALLET).add(ModItems.WALLET.get());
        tag(ModTags.Items.TOOL_KEY).add(ModItems.ENGINEER_KEY.get());

    }
}
