package com.grimmyboi.javaengineering.crafting.recipe;

import com.google.gson.JsonObject;
import com.grimmyboi.javaengineering.setup.ModRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SingleItemRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class DeliveryRecipe extends SingleItemRecipe {
    public DeliveryRecipe(ResourceLocation recipeId, Ingredient ingredient, ItemStack result) {

        super(ModRecipes.Types.DELIVERY, ModRecipes.Serializers.Delivery.get(), recipeId, "", ingredient, result);
    }

    @Override
    public boolean matches(IInventory inv, World world) {
        return this.ingredient.test(inv.getItem(0));
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<DeliveryRecipe>{

        @Override
        public DeliveryRecipe fromJson(ResourceLocation recipeId, JsonObject jsonObject) {
            Ingredient ingredient=Ingredient.fromJson(jsonObject.get("ingredient"));
            ResourceLocation itemId=new ResourceLocation(JSONUtils.getAsString(jsonObject, "result"));
            int count=JSONUtils.getAsInt(jsonObject,"count",1);

            ItemStack result = new ItemStack((ForgeRegistries.ITEMS.getValue(itemId)),count);
            return new DeliveryRecipe(recipeId,ingredient,result);
        }

        @Nullable
        @Override
        public DeliveryRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
                Ingredient ingredient=Ingredient.fromNetwork(buffer);
                ItemStack result= buffer.readItem();
                return new DeliveryRecipe(recipeId,ingredient,result);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, DeliveryRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
        }
    }
}
