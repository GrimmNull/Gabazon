package com.grimmyboi.javaengineering.block.thermalgenerator;

import com.grimmyboi.javaengineering.block.AbstractModEntityTile;
import com.grimmyboi.javaengineering.setup.Config;
import com.grimmyboi.javaengineering.setup.ModTileEntityTypes;
import com.grimmyboi.javaengineering.tools.CustomEnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.util.LazyOptional;

public class ThermalGeneratorTileEntity extends AbstractModEntityTile {
    public ThermalGeneratorTileEntity() {
        super(ModTileEntityTypes.THERMAL_GENERATOR.get());
        energyStorage = createEnergy(Config.THERMALGENERATOR_MAXPOWER.get(), 500);
        energy = LazyOptional.of(() -> energyStorage);
    }

    private CustomEnergyStorage energyStorageOff = createEnergy(Config.THERMALGENERATOR_MAXPOWER.get(), 0);


    private Boolean lastPoweredBlockState = false, active = true;


    @Override
    public void tick() {
        if (level != null && level.isClientSide) {
            return;
        }
        if (!active) {
            if (active != lastPoweredBlockState) {
                lastPoweredBlockState = false;
                energy = LazyOptional.of(() -> energyStorageOff);
            }
            return;
        } else if (active != lastPoweredBlockState) {
            energy = LazyOptional.of(() -> energyStorage);
            lastPoweredBlockState = true;
        }
        if (counter > 0) {
            counter--;
            if (counter <= 0) {
                energyStorage.addEnergy(Config.THERMALGENERATOR_GENERATE.get());
            }
            setChanged();
        }

        if (counter <= 0) {
            ItemStack stack = itemHandler.getStackInSlot(0);
            if (stack.getItem() == Items.LAVA_BUCKET) {
                itemHandler.extractItem(0, 1, false);
                counter = Config.THERMALGENERATOR_TICKS.get();
                setChanged();
            }
        }
    }


    @Override
    protected Boolean conditionToCheck(ItemStack stack){
        return stack.getItem() == Items.LAVA_BUCKET;
    }

    public void setActive(Boolean state) {
        this.active = state;
    }

    public Boolean getActive() {
        return this.active;
    }


}
