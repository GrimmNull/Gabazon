package com.grimmyboi.javaengineering.setup;

import com.grimmyboi.javaengineering.block.energypipe.EnergyPipeTileEntity;
import com.grimmyboi.javaengineering.block.fivegantenna.FiveGAntennaTileEntity;
import com.grimmyboi.javaengineering.block.gabazonstation.GabazonStationTileEntity;
import com.grimmyboi.javaengineering.block.monitor.MonitorTileEntity;
import com.grimmyboi.javaengineering.block.thermalgenerator.ThermalGeneratorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class ModTileEntityTypes {
    public static final RegistryObject<TileEntityType<ThermalGeneratorTileEntity>> THERMAL_GENERATOR= register("thermal_generator",
            ThermalGeneratorTileEntity::new,
            ModBlocks.THERMAL_GENERATOR);
    public static final RegistryObject<TileEntityType<GabazonStationTileEntity>> GABAZON_STATION= register("gabazon_station",
            GabazonStationTileEntity::new,
            ModBlocks.GABAZON_STATION);
    public static final RegistryObject<TileEntityType<EnergyPipeTileEntity>> ENERGY_PIPE= register("energy_pipe",
            EnergyPipeTileEntity::new,
            ModBlocks.ENERGY_PIPE);
    public static final RegistryObject<TileEntityType<FiveGAntennaTileEntity>> FIVE_G_ANTENNA= register("five_g_antenna",
            FiveGAntennaTileEntity::new,
            ModBlocks.FIVE_G_ANTENNA);
    public static final RegistryObject<TileEntityType<MonitorTileEntity>> MONITOR= register("monitor",
            MonitorTileEntity::new,
            ModBlocks.MONITOR);

    static void register(){}

    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factory, RegistryObject<? extends Block> block){
        return Registration.TILE_ENTITIES.register(name,()->{
            return TileEntityType.Builder.of(factory,block.get()).build(null);
        });
    }
}
