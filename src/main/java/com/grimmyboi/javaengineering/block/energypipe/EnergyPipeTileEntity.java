package com.grimmyboi.javaengineering.block.energypipe;

import com.grimmyboi.javaengineering.block.AbstractModEntityTile;
import com.grimmyboi.javaengineering.setup.Config;
import com.grimmyboi.javaengineering.setup.ModTileEntityTypes;
import net.minecraftforge.common.util.LazyOptional;

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
                    energyFlow(Config.ENERGYPIPE_RECEIVE.get(),50);
            }
        }
        updateBlockState();
    }


}
