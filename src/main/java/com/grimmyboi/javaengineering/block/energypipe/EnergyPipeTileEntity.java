package com.grimmyboi.javaengineering.block.energypipe;

import com.grimmyboi.javaengineering.block.AbstractModEntityTile;
import com.grimmyboi.javaengineering.setup.Config;
import com.grimmyboi.javaengineering.setup.ModTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import java.util.concurrent.atomic.AtomicInteger;

public class EnergyPipeTileEntity extends AbstractModEntityTile {
    public EnergyPipeTileEntity() {
        super(ModTileEntityTypes.ENERGY_PIPE.get());
        energyStorage = createEnergy(Config.ENERGYPIPE_MAXPOWER.get(), 100);
        energy = LazyOptional.of(() -> energyStorage);
    }

    private int counter;


    @Override
    public void tick() {
        if (level != null && level.isClientSide) {
            return;
        }
        if(energyStorage.getEnergyStored()< energyStorage.getMaxEnergyStored()) {
            if (counter > 0) {
                counter--;
                if (counter <= 0) {
                    energyStorage.addEnergy(Config.ENERGYPIPE_RECEIVE.get());
                }
                setChanged();
            }

            if (counter <= 0) {
                AtomicInteger capacity = new AtomicInteger(energyStorage.getEnergyStored());
                for (Direction direction : Direction.values()) {
                    TileEntity te = level.getBlockEntity(worldPosition.relative(direction));
                    if (te != null) {
                        boolean doContinue = te.getCapability(CapabilityEnergy.ENERGY, direction).map(handler -> {
                                    if (handler.getEnergyStored() > 0) {
                                        int received = handler.extractEnergy(Math.min(handler.getEnergyStored(), Config.ENERGYPIPE_RECEIVE.get()), false);
                                        capacity.addAndGet(received);
                                        energyStorage.addEnergy(received);
                                        counter = 50;
                                        setChanged();
                                        return capacity.get() > 0;
                                    } else {
                                        return true;
                                    }
                                }
                        ).orElse(true);
                        if (!doContinue) {
                            return;
                        }
                    }
                }
            }
        }
        updateBlockState();
    }


}
