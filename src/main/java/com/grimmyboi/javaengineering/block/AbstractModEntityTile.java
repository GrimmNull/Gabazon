package com.grimmyboi.javaengineering.block;

import com.grimmyboi.javaengineering.setup.Config;
import com.grimmyboi.javaengineering.tools.CustomEnergyStorage;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractModEntityTile extends TileEntity implements ITickableTileEntity {
    public ItemStackHandler itemHandler = createHandler();
    public CustomEnergyStorage energyStorage = createEnergy(Config.THERMALGENERATOR_MAXPOWER.get(), 500);

    public LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    public LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);


    public int counter;

    public AbstractModEntityTile(TileEntityType block) {
        super(block);
    }

    protected void energyFlow(Integer valueForMin,Integer ticks){
        AtomicInteger capacity = new AtomicInteger(energyStorage.getEnergyStored());
        for (Direction direction : Direction.values()) {
            TileEntity te = level.getBlockEntity(worldPosition.relative(direction));
            if (te != null) {
                boolean doContinue = te.getCapability(CapabilityEnergy.ENERGY, direction).map(handler -> {
                            if (handler.getEnergyStored() > 0) {
                                int received = handler.extractEnergy(Math.min(handler.getEnergyStored(), valueForMin), false);
                                capacity.addAndGet(received);
                                counter = ticks;
                                energyStorage.addEnergy(received);
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

    protected void updateBlockState(){
        BlockState blockState = level.getBlockState(worldPosition);
        if (blockState.getValue(BlockStateProperties.POWERED) != energyStorage.getEnergyStored()>0) {
            level.setBlock(worldPosition, blockState.setValue(BlockStateProperties.POWERED, energyStorage.getEnergyStored()>0),
                    Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.BLOCK_UPDATE);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        handler.invalidate();
        energy.invalidate();
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        itemHandler.deserializeNBT(tag.getCompound("inv"));
        energyStorage.deserializeNBT(tag.getCompound("energy"));

        counter = tag.getInt("counter");
        super.load(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.put("inv", itemHandler.serializeNBT());
        tag.put("energy", energyStorage.serializeNBT());

        tag.putInt("counter", counter);
        return super.save(tag);
    }

    protected Boolean conditionToCheck(ItemStack stack){
        return true;
    }

    protected ItemStackHandler createHandler() {
        return new ItemStackHandler(1) {

            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return conditionToCheck(stack);
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (!conditionToCheck(stack)) {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    protected CustomEnergyStorage createEnergy(int capacity, int transfer) {
        return new CustomEnergyStorage(capacity, transfer) {
            @Override
            protected void onEnergyChanged() {
                setChanged();
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        if (cap == CapabilityEnergy.ENERGY) {
            return energy.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void tick() {

    }
}
