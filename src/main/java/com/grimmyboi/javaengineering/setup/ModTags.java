package com.grimmyboi.javaengineering.setup;

import com.grimmyboi.javaengineering.Main;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class ModTags {
    public static final class Blocks{
        public static final ITag.INamedTag<Block> REDSTONE_GENERATOR =forge("redstone/generator");
        public static final ITag.INamedTag<Block> REDSTONE_PIPE =forge("redstone/pipe");
        public static final ITag.INamedTag<Block> REDSTONE_MONITOR =forge("redstone/monitor");
        public static final ITag.INamedTag<Block> REDSTONE_STATION =forge("redstone/station");
        public static final ITag.INamedTag<Block> REDSTONE_ANTENNA =forge("redstone/antenna");

        private static ITag.INamedTag<Block> forge(String path){
            return BlockTags.bind(new ResourceLocation("forge",path).toString());
        }

        private static ITag.INamedTag<Block> mod(String path){
            return BlockTags.bind(new ResourceLocation(Main.MOD_ID, path).toString());
        }
    }

    public static final class Items{
        public static final ITag.INamedTag<Item> REDSTONE_GENERATOR =forge("redstone/generator");
        public static final ITag.INamedTag<Item> REDSTONE_PIPE =forge("redstone/pipe");
        public static final ITag.INamedTag<Item> REDSTONE_MONITOR =forge("redstone/monitor");
        public static final ITag.INamedTag<Item> REDSTONE_STATION =forge("redstone/station");
        public static final ITag.INamedTag<Item> REDSTONE_ANTENNA =forge("redstone/antenna");

        public static final ITag.INamedTag<Item> TOOL_KEY =forge("tool/key");
        public static final ITag.INamedTag<Item> TOOL_WALLET =forge("tool/wallet");

        private static ITag.INamedTag<Item> forge(String path){
            return ItemTags.bind(new ResourceLocation("forge",path).toString());
        }

        private static ITag.INamedTag<Item> mod(String path){
            return ItemTags.bind(new ResourceLocation(Main.MOD_ID, path).toString());
        }
    }
}
