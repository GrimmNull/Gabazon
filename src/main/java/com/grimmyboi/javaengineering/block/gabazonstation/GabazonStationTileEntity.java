package com.grimmyboi.javaengineering.block.gabazonstation;

import com.grimmyboi.javaengineering.Main;
import com.grimmyboi.javaengineering.block.AbstractModEntityTile;
import com.grimmyboi.javaengineering.setup.Config;
import com.grimmyboi.javaengineering.setup.ModTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicInteger;

public class GabazonStationTileEntity extends AbstractModEntityTile {
    private int money = 0;

    public GabazonStationTileEntity() {
        super(ModTileEntityTypes.GABAZON_STATION.get());
        energyStorage = createEnergy(Config.GABAZONSTATION_MAXPOWER.get(), 100);
        energy = LazyOptional.of(() -> energyStorage);
    }

    @Override
    public void tick() {
        if (level != null && level.isClientSide) {
            return;
        }

        ItemStack stack = itemHandler.getStackInSlot(0);
        if (Main.doesItHaveValue(stack.getItem())) {
            money += Main.getItemPrice(stack.getItem());
            itemHandler.extractItem(0, 1, false);
            System.out.println("Money: " + money);
        }
        if(energyStorage.getEnergyStored()< energyStorage.getMaxEnergyStored()) {
        if (counter > 0) {
            counter--;
            if (counter <= 0) {
                energyStorage.addEnergy(Config.GABAZONSTATION_RECEIVE.get());
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
                                    int received = handler.extractEnergy(Math.min(handler.getEnergyStored(), Config.GABAZONSTATION_RECEIVE.get()), false);
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
        energyStorage.consumeEnergy(Config.MONITOR_CONSUME_PER_TICK.get());
    }

    @Override
    protected Boolean conditionToCheck(ItemStack stack) {
        return Main.doesItHaveValue(stack.getItem());
    }

    public int getMoney() {
        return this.money;
    }

}
