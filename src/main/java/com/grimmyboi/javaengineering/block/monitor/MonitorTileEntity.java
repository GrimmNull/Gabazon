package com.grimmyboi.javaengineering.block.monitor;

import com.grimmyboi.javaengineering.block.gabazonstation.GabazonStationTileEntity;
import com.grimmyboi.javaengineering.setup.Config;
import com.grimmyboi.javaengineering.setup.GabazonClient;
import com.grimmyboi.javaengineering.setup.ModTileEntityTypes;
import com.grimmyboi.javaengineering.tools.CustomEnergyStorage;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.server.management.PlayerList;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class MonitorTileEntity extends TileEntity implements ITickableTileEntity {
    private ItemStackHandler itemHandler = createHandler();
    private CustomEnergyStorage energyStorage = createEnergy(Config.MONITOR_MAXPOWER.get(), 100);

    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);

    private int counter;

    public MonitorTileEntity() {
        super(ModTileEntityTypes.MONITOR.get());
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        handler.invalidate();
        energy.invalidate();
    }

    @Override
    public void tick() {
        if (level != null && level.isClientSide) {
            return;
        }

        ItemStack stack = itemHandler.getStackInSlot(0);
        if (stack.getItem()== Items.WRITTEN_BOOK) {
            CompoundNBT nbt=stack.getItem().getShareTag(stack);
            String temp=nbt.get("pages").getAsString().replace("\\\\n"," ")
                                                      .replace("\"text\":","")
                                                      .replace("['{","")
                                                      .replace("}']","")
                                                      .replace(","," ")
                                                      .replace("\"}'","")
                                                      .replace("'{\"","");
            try {
                Integer money=0;
                for (Direction direction : Direction.values()) {
                    TileEntity te = level.getBlockEntity(worldPosition.relative(direction));
                    if(te instanceof GabazonStationTileEntity){
                        money=((GabazonStationTileEntity) te).getMoney();
                    }
                }
                String answer=GabazonClient.sendCommandToServer(temp,money);
                if(answer.contains("Error:")){
                    sendBookMessage(stack,answer,"Order error",true);
                    return;
                } else{
                    String buget,pret;
                    buget=answer.split(" ")[0];
                    pret=answer.split(" ")[1];
                    sendBookMessage(stack,"The order was " + pret + " and now you have a balance of:" +buget,"Order completed",false);
                    answer=answer.replace(buget,"").replace(pret,"");
                    System.out.println(answer);
                    for(String product : answer.split(" ")){
                        product.replaceAll(" ","");
                        if(product.length()==0) continue;

                        product=product.replace("x"," ");
                        level.addFreshEntity(new ItemEntity(level,worldPosition.getX(),worldPosition.getY(),worldPosition.getZ()+1 ,new ItemStack(
                                ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft",product.split(" ")[1].replace("minecraft:",""))), Integer.parseInt(product.split(" ")[0])
                                                )));
                    }
                }
            } catch (IOException e) {
                sendBookMessage(stack,"I couldn't get to the server to retrieve information","Connection error",true);
            }
            itemHandler.extractItem(0, 1, false);
        }

        if (counter > 0) {
            counter--;
            if (counter <= 0) {
                energyStorage.addEnergy(Config.MONITOR_RECEIVE.get());
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
                                    int received = handler.extractEnergy(Math.min(handler.getEnergyStored(), Config.MONITOR_RECEIVE.get()), false);
                                    //System.out.println("Monitor:I can extract");
                                    capacity.addAndGet(received);
                                    counter = received/50;
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

        BlockState blockState = level.getBlockState(worldPosition);
        if (blockState.getValue(BlockStateProperties.POWERED) != counter > 0) {
            level.setBlock(worldPosition, blockState.setValue(BlockStateProperties.POWERED, counter > 0),
                    Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.BLOCK_UPDATE);
        }

    }

    private void sendBookMessage(ItemStack stack,String errorMessage,String errorTitle,Boolean simulatedPop) {
        ItemStack tomeStack = new ItemStack(Items.WRITTEN_BOOK);
        ListNBT bookPages = new ListNBT();
        bookPages.add(StringNBT.valueOf("\\n"+ errorMessage));
        tomeStack.addTagElement("pages", bookPages);
        tomeStack.addTagElement("author", StringNBT.valueOf("GabazonStation"));
        tomeStack.addTagElement("title", StringNBT.valueOf(errorTitle));
        level.addFreshEntity(new ItemEntity(level,worldPosition.getX(),worldPosition.getY(),worldPosition.getZ()+1 ,tomeStack));
        if(simulatedPop)
        level.addFreshEntity(new ItemEntity(level,worldPosition.getX(),worldPosition.getY(),worldPosition.getZ()+1 ,stack));
        itemHandler.extractItem(0,1,false);
        return;
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

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(1) {

            @Override
            protected void onContentsChanged(int slot) {
                // To make sure the TE persists when the chunk is saved later we need to
                // mark it dirty every time the item handler changes
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return stack.getItem()==Items.WRITTEN_BOOK;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (stack.getItem()!=Items.WRITTEN_BOOK) {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    private CustomEnergyStorage createEnergy(int capacity, int transfer) {
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
}
