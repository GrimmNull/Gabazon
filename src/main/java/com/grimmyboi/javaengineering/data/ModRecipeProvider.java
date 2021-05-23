package com.grimmyboi.javaengineering.data;

import com.grimmyboi.javaengineering.setup.ModBlocks;
import com.grimmyboi.javaengineering.setup.ModItems;
import net.minecraft.data.*;
import net.minecraft.item.Items;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator p_i48262_1_) {
        super(p_i48262_1_);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(ModItems.ENGINEER_KEY.get())
                .define('i', Items.IRON_INGOT)
                .pattern("i i")
                .pattern("iii")
                .pattern(" i ")
                .unlockedBy("has_item", has(Items.IRON_INGOT)).group("redstone")
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.WALLET.get())
                .define('l', Items.LEATHER)
                .define('d', Items.DIAMOND)
                .pattern("lll")
                .pattern("ldl")
                .pattern("lll")
                .unlockedBy("has_item", has(Items.DIAMOND)).group("redstone")
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.FIVE_G_ANTENNA.get())
                .define('i', Items.IRON_INGOT)
                .pattern("iii")
                .pattern(" i ")
                .pattern("iii")
                .unlockedBy("has_item", has(Items.IRON_INGOT)).group("redstone")
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.ENERGY_PIPE.get(), 3)
                .define('i', Items.IRON_INGOT)
                .define('r', Items.REDSTONE)
                .pattern("iii")
                .pattern("rrr")
                .pattern("iii")
                .unlockedBy("has_item", has(ModBlocks.ENERGY_PIPE.get())).group("redstone")
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.GABAZON_STATION.get())
                .define('d', Items.DIAMOND_BLOCK)
                .define('r', Items.REDSTONE)
                .define('i', Items.IRON_INGOT)
                .pattern("iri")
                .pattern("rdr")
                .pattern("iri")
                .unlockedBy("has_item", has(ModBlocks.GABAZON_STATION.get())).group("redstone")
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.MONITOR.get())
                .define('i', Items.IRON_INGOT)
                .define('g', Items.GLASS)
                .define('r', Items.REDSTONE)
                .pattern("iii")
                .pattern("igi")
                .pattern("iri")
                .unlockedBy("has_item", has(ModBlocks.MONITOR.get())).group("redstone")
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModBlocks.THERMAL_GENERATOR.get())
                .define('l', Items.LAVA_BUCKET)
                .define('w', Items.WATER_BUCKET)
                .define('r', Items.REDSTONE)
                .define('i', Items.IRON_INGOT)
                .define('o', Items.OBSIDIAN)
                .pattern("low")
                .pattern("oro")
                .pattern("iii")
                .unlockedBy("has_item", has(ModBlocks.THERMAL_GENERATOR.get())).group("redstone")
                .save(consumer);

    }
}
