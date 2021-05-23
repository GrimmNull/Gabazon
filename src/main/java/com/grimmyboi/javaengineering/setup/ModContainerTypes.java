package com.grimmyboi.javaengineering.setup;

import com.grimmyboi.javaengineering.block.gabazonstation.GabazonStationContainer;
import com.grimmyboi.javaengineering.block.monitor.MonitorContainer;
import com.grimmyboi.javaengineering.block.thermalgenerator.ThermalGeneratorContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;

public class ModContainerTypes {
    public static final RegistryObject<ContainerType<ThermalGeneratorContainer>> THERMAL_GENERATOR_CONTAINER = Registration.CONTAINERS.register("thermal_generator", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getCommandSenderWorld();
        return new ThermalGeneratorContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<ContainerType<GabazonStationContainer>> GABAZON_STATION_CONTAINER = Registration.CONTAINERS.register("gabazon_station", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getCommandSenderWorld();
        return new GabazonStationContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<ContainerType<MonitorContainer>> MONITOR_CONTAINER = Registration.CONTAINERS.register("monitor", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getCommandSenderWorld();
        return new MonitorContainer(windowId, world, pos, inv, inv.player);
    }));

    static void register() {
    }
}
