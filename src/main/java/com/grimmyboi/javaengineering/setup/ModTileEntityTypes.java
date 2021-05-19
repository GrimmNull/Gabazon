package com.grimmyboi.javaengineering.setup;

import com.grimmyboi.javaengineering.block.thermalgenerator.ThermalGeneratorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class ModTileEntityTypes {
    public static final RegistryObject<TileEntityType<ThermalGeneratorTileEntity>> THERMAL_GENERATOR= register("thermal_generator",
            ThermalGeneratorTileEntity::new,
            ModBlocks.THERMAL_GENERATOR_BLOCK);

    static void register(){}

    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factory, RegistryObject<? extends Block> block){
        return Registration.TILE_ENTITIES.register(name,()->{
            return TileEntityType.Builder.of(factory,block.get()).build(null);
        });
    }
}
