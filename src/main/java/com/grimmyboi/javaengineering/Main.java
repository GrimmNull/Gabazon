package com.grimmyboi.javaengineering;

import com.grimmyboi.javaengineering.setup.ClientSetup;
import com.grimmyboi.javaengineering.setup.Registration;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.ArrayList;
import java.util.List;

@Mod(Main.MOD_ID)
public class Main {
    public static final String MOD_ID="javaengineering";
    private static List<Item> itemsWithValue=new ArrayList<>();
    private static List<Integer> pricesOfItems=new ArrayList<>();


    public void addItemsToList(){
        itemsWithValue.add(Items.DIAMOND);
        pricesOfItems.add(200);
        itemsWithValue.add(Items.COAL);
        pricesOfItems.add(50);
        itemsWithValue.add(Items.CHARCOAL);
        pricesOfItems.add(30);
        itemsWithValue.add(Items.IRON_INGOT);
        pricesOfItems.add(80);
        itemsWithValue.add(Items.GOLD_INGOT);
        pricesOfItems.add(120);
        itemsWithValue.add(Items.EMERALD);
        pricesOfItems.add(250);
        itemsWithValue.add(Items.GOLDEN_APPLE);
        pricesOfItems.add(500);
        itemsWithValue.add(Items.ENDER_PEARL);
        pricesOfItems.add(100);
        itemsWithValue.add(Items.NETHERITE_INGOT);
        pricesOfItems.add(300);
    }

    public static Boolean doesItHaveValue(Item itemToTest){
        return itemsWithValue.contains(itemToTest);
    }

    public static int getItemPrice(Item itemToSell){
        return pricesOfItems.get(itemsWithValue.indexOf(itemToSell));
    }

    public Main() {
        addItemsToList();
        Registration.register();
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
    }


}
