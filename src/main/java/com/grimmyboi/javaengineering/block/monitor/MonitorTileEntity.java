package com.grimmyboi.javaengineering.block.monitor;

import com.grimmyboi.javaengineering.block.AbstractModEntityTile;
import com.grimmyboi.javaengineering.block.gabazonstation.GabazonStationTileEntity;
import com.grimmyboi.javaengineering.setup.Config;
import com.grimmyboi.javaengineering.setup.GabazonClient;
import com.grimmyboi.javaengineering.setup.ModTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class MonitorTileEntity extends AbstractModEntityTile {
    public MonitorTileEntity() {
        super(ModTileEntityTypes.MONITOR.get());
        energyStorage = createEnergy(Config.MONITOR_MAXPOWER.get(), 0);
        energy = LazyOptional.of(() -> energyStorage);
    }

    @Override
    public void tick() {
        if (level != null && level.isClientSide) {
            return;
        }
        processItem();
        if(energyStorage.getEnergyStored()< energyStorage.getMaxEnergyStored()) {
            if (counter > 0) {
                counter--;
                if (counter <= 0) {
                }
                setChanged();
            }
            if (counter <= 0) {
                    energyFlow(Config.MONITOR_RECEIVE.get(),40);
            }
        }
        updateBlockState();
        energyStorage.consumeEnergy(Config.MONITOR_CONSUME_PER_TICK.get());
    }

    private void processItem(){
        ItemStack stack = itemHandler.getStackInSlot(0);
        if (stack.getItem() == Items.WRITTEN_BOOK) {
            CompoundNBT nbt = stack.getItem().getShareTag(stack);
            String temp = nbt.get("pages").getAsString().replace("\\\\n", " ")
                    .replace("\"text\":", "")
                    .replace("['{", "")
                    .replace("}']", "")
                    .replace(",", " ")
                    .replace("\"}'", "")
                    .replace("'{\"", "");
            try {
                Integer money = 0;
                for (Direction direction : Direction.values()) {
                    TileEntity te = level.getBlockEntity(worldPosition.relative(direction));
                    if (te instanceof GabazonStationTileEntity) {
                        money = ((GabazonStationTileEntity) te).getMoney();
                    }
                }
                String answer = GabazonClient.sendCommandToServer(temp, money);
                if (answer.contains("Error:")) {
                    sendBookMessage(stack, answer, "Order error", true);
                    return;
                } else {
                    String buget, pret;
                    buget = answer.split(" ")[0];
                    pret = answer.split(" ")[1];
                    sendBookMessage(stack, "The order was " + pret + " and now you have a balance of:" + buget, "Order completed", false);
                    answer = answer.replace(buget, "").replace(pret, "");
                    for (String product : answer.split(" ")) {
                        product.replaceAll(" ", "");
                        if (product.length() == 0) continue;

                        product = product.replace("x", " ");
                        level.addFreshEntity(new ItemEntity(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ() + 1, new ItemStack(
                                ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft", product.split(" ")[1].replace("minecraft:", ""))), Integer.parseInt(product.split(" ")[0])
                        )));
                    }
                }
            } catch (IOException e) {
                sendBookMessage(stack, "I couldn't get to the server to retrieve information", "Connection error", true);
            }
            itemHandler.extractItem(0, 1, false);
        }
    }

    private void sendBookMessage(ItemStack stack, String errorMessage, String errorTitle, Boolean simulatedPop) {
        ItemStack tomeStack = new ItemStack(Items.WRITTEN_BOOK);
        ListNBT bookPages = new ListNBT();
        bookPages.add(StringNBT.valueOf("\\n" + errorMessage));
        tomeStack.addTagElement("pages", bookPages);
        tomeStack.addTagElement("author", StringNBT.valueOf("GabazonStation"));
        tomeStack.addTagElement("title", StringNBT.valueOf(errorTitle));
        level.addFreshEntity(new ItemEntity(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ() + 1, tomeStack));
        if (simulatedPop)
            level.addFreshEntity(new ItemEntity(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ() + 1, stack));
        itemHandler.extractItem(0, 1, false);
        return;
    }


    @Override
    protected Boolean conditionToCheck(ItemStack stack) {
        return stack.getItem() == Items.WRITTEN_BOOK;
    }
}
