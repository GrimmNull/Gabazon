package com.grimmyboi.javaengineering.setup;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class ModItems {
    public static final RegistryObject<Item> ENGINEER_KEY= Registration.ITEMS.register("engineer_key",() ->
            new Item(new Item.Properties().tab(ItemGroup.TAB_REDSTONE).stacksTo(1)));
    public static final RegistryObject<Item> WALLET=Registration.ITEMS.register("wallet", ()->
            new Item(new Item.Properties().tab(ItemGroup.TAB_REDSTONE).stacksTo(1)));
    static void register(){

    }
}
