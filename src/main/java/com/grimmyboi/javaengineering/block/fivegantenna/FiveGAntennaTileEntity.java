package com.grimmyboi.javaengineering.block.fivegantenna;

import com.grimmyboi.javaengineering.block.AbstractModEntityTile;
import com.grimmyboi.javaengineering.setup.Config;
import com.grimmyboi.javaengineering.setup.ModTileEntityTypes;
import net.minecraftforge.common.util.LazyOptional;


public class FiveGAntennaTileEntity extends AbstractModEntityTile {
    public FiveGAntennaTileEntity() {
        super(ModTileEntityTypes.FIVE_G_ANTENNA.get());
        energyStorage = createEnergy(Config.GABAZONSTATION_MAXPOWER.get(), 0);
        energy = LazyOptional.of(() -> energyStorage);
    }


}
