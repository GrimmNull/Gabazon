package com.grimmyboi.javaengineering.setup;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final RegistryObject<Block> THERMAL_GENERATOR=register("thermal_generator",()->
            new Block(AbstractBlock.Properties.of(Material.HEAVY_METAL)
                    .harvestLevel(2)
                    .harvestTool(ToolType.PICKAXE)
                    .sound(SoundType.METAL)
            ));
    public static final RegistryObject<Block> FIVE_G_ANTENNA=register("five_g_antenna", ()->
            new Block(AbstractBlock.Properties.of(Material.HEAVY_METAL)
                    .harvestLevel(2)
                    .harvestTool(ToolType.PICKAXE)
                    .sound(SoundType.ANVIL)
            ));
    public static final RegistryObject<Block> MONITOR=register("monitor", ()->
            new Block(AbstractBlock.Properties.of(Material.HEAVY_METAL)
                    .harvestLevel(1)
                    .harvestTool(ToolType.PICKAXE)
                    .sound(SoundType.GLASS)
            ));
    public static final RegistryObject<Block> GABAZON_STATION=register("gabazon_station", ()->
            new Block(AbstractBlock.Properties.of(Material.HEAVY_METAL)
                    .harvestLevel(2)
                    .harvestTool(ToolType.PICKAXE)
                    .sound(SoundType.METAL)
            ));
    public static final RegistryObject<Block> ENERGY_PIPE=register("energy_pipe", ()->
            new Block(AbstractBlock.Properties.of(Material.HEAVY_METAL)
                    .harvestLevel(2)
                    .harvestTool(ToolType.PICKAXE)
                    .sound(SoundType.ANVIL)
            ));

    public static void register(){}

    public static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> block){
        return Registration.BLOCKS.register(name,block);
    }

    public static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block){
        RegistryObject<T> ret=registerNoItem(name,block);
        Registration.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE)));
        return ret;
    }


}
