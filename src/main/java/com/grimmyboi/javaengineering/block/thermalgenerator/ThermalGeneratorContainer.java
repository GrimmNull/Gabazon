package com.grimmyboi.javaengineering.block.thermalgenerator;

import com.grimmyboi.javaengineering.block.AbstractModContainer;
import com.grimmyboi.javaengineering.setup.ModBlocks;
import com.grimmyboi.javaengineering.setup.ModContainerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ThermalGeneratorContainer extends AbstractModContainer {
    public ThermalGeneratorContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(ModContainerTypes.THERMAL_GENERATOR_CONTAINER.get(), windowId);
        tileEntity = world.getBlockEntity(pos);
        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 64, 24));
            });
        }
        layoutPlayerInventorySlots(10, 70);
        trackPower();
    }


    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return stillValid(IWorldPosCallable.create(tileEntity.getLevel(), tileEntity.getBlockPos()), playerEntity, ModBlocks.THERMAL_GENERATOR.get());
    }

    @Override
    protected Boolean conditionToCheck(ItemStack stack) {
        return stack.getItem() == Items.LAVA_BUCKET;
    }


}
