package com.grimmyboi.javaengineering.setup;

import com.grimmyboi.javaengineering.Main;
import com.grimmyboi.javaengineering.crafting.recipe.DeliveryRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class ModRecipes {
    public static class Types{
        public static final IRecipeType<DeliveryRecipe> DELIVERY=IRecipeType.register(Main.MOD_ID + "delivery");
    }
    public static class Serializers{
        public static final RegistryObject<IRecipeSerializer<DeliveryRecipe>> Delivery=register("delivery", DeliveryRecipe.Serializer::new);

        private static <T extends IRecipe<?>>RegistryObject<IRecipeSerializer<T>> register(String name, Supplier<IRecipeSerializer<T>> serializer){
            return Registration.RECIPE_SERIALIZERS.register(name,serializer);
        }
    }
    static void register(){ }
}
