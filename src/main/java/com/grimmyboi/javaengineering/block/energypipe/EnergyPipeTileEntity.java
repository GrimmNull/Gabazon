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
                    energyFlow(Config.ENERGYPIPE_RECEIVE.get(),50);
            }
        }
        updateBlockState();
    }


}
