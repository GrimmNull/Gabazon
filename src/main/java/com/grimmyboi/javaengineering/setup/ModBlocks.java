package com.grimmyboi.javaengineering.setup;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final RegistryObject<Block> THERMAL_GENERATOR=register("thermal_generator",()->
            new Block(AbstractBlock.Properties.of(Material.HEAVY_METAL)
                    .harvestLevel(2)
                    .harvestTool(ToolType.PICKAXE)
                    .sound(SoundType.METAL)
                    .strength(3,10)
                    .requiresCorrectToolForDrops()
                    .isRedstoneConductor(new AbstractBlock.IPositionPredicate() {
                        @Override
                        public boolean test(BlockState p_test_1_, IBlockReader p_test_2_, BlockPos p_test_3_) {
                            return false;
                        }
                    })
            ));
    public static final RegistryObject<Block> FIVE_G_ANTENNA=register("five_g_antenna", ()->
            new Block(AbstractBlock.Properties.of(Material.HEAVY_METAL)
                    .harvestLevel(2)
                    .harvestTool(ToolType.PICKAXE)
                    .sound(SoundType.ANVIL)
                    .requiresCorrectToolForDrops()
                    .strength(3,10)
            ));
    public static final RegistryObject<Block> MONITOR=register("monitor", ()->
            new Block(AbstractBlock.Properties.of(Material.HEAVY_METAL)
                    .harvestLevel(1)
                    .harvestTool(ToolType.PICKAXE)
                    .sound(SoundType.GLASS)
                    .requiresCorrectToolForDrops()
                    .strength(3,10)
            ));
    public static final RegistryObject<Block> GABAZON_STATION=register("gabazon_station", ()->
            new Block(AbstractBlock.Properties.of(Material.HEAVY_METAL)
                    .harvestLevel(2)
                    .harvestTool(ToolType.PICKAXE)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(3,10)
                    .isRedstoneConductor(new AbstractBlock.IPositionPredicate() {
                        @Override
                        public boolean test(BlockState p_test_1_, IBlockReader p_test_2_, BlockPos p_test_3_) {
                            return false;
                        }
                    })
            ));
    public static final RegistryObject<Block> ENERGY_PIPE=register("energy_pipe", ()->
            new Block(AbstractBlock.Properties.of(Material.HEAVY_METAL)
                    .harvestLevel(2)
                    .harvestTool(ToolType.PICKAXE)
                    .sound(SoundType.ANVIL)
                    .requiresCorrectToolForDrops()
                    .strength(3,10)
                    .isRedstoneConductor(new AbstractBlock.IPositionPredicate() {
                        @Override
                        public boolean test(BlockState p_test_1_, IBlockReader p_test_2_, BlockPos p_test_3_) {
                            return false;
                        }
                    })
            ));

    public static void register(){}

    public static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> block){
        return Registration.BLOCKS.register(name,block);
    }

    public static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block){
        RegistryObject<T> ret=registerNoItem(name,block);
        Registration.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().tab(ItemGroup.TAB_REDSTONE).stacksTo(6)));
        return ret;
    }


}
