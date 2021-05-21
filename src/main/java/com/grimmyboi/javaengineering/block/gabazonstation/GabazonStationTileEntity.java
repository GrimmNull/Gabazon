package com.grimmyboi.javaengineering.block.gabazonstation;

import com.grimmyboi.javaengineering.block.thermalgenerator.ThermalGeneratorContainer;
import com.grimmyboi.javaengineering.crafting.recipe.DeliveryRecipe;
import com.grimmyboi.javaengineering.setup.ModRecipes;
import com.grimmyboi.javaengineering.setup.ModTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;

public class GabazonStationTileEntity extends LockableTileEntity implements ISidedInventory, ITickableTileEntity {
    static final int DELIVERY_TIME = 2 * 20; // nr of seconds * 20 ticks

    private NonNullList<ItemStack> items;
    private final LazyOptional<? extends IItemHandler>[] handlers;

    private int progress = 0;

    private final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return progress;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    progress = value;
                    break;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public GabazonStationTileEntity() {
        super(ModTileEntityTypes.GABAZON_STATION.get());
        this.handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
        this.items = NonNullList.withSize(2, ItemStack.EMPTY);
    }

    void encodeExtraData(PacketBuffer buffer) {
        buffer.writeByte(fields.getCount());
    }

    @Override
    public void tick() {
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        DeliveryRecipe recipe = getRecipe();
        if (recipe != null) {
            doWork(recipe);
        } else {
            stopDelivery();
        }
    }

    @Nullable
    public DeliveryRecipe getRecipe() {
        if (this.level == null || getItem(0).isEmpty())
            return null;
        return this.level.getRecipeManager().getRecipeFor(ModRecipes.Types.DELIVERY, this, this.level).orElse(null);
    }

    private ItemStack getWorkOutput(@Nullable DeliveryRecipe recipe) {
        if (recipe != null)
            return recipe.assemble(this);
        return ItemStack.EMPTY;
    }

    private void doWork(DeliveryRecipe recipe) {
        assert this.level != null;

        ItemStack current = getItem(1);
        ItemStack output = getWorkOutput(recipe);

        if (!current.isEmpty()) {
            int newCount = current.getCount() + output.getCount();

            if (!ItemStack.matches(current, output) || newCount > output.getMaxStackSize()) {
                stopDelivery();
                return;
            }

        }
        if (progress < DELIVERY_TIME) {
            progress++;
        }
        if (progress >= DELIVERY_TIME && level.isClientSide) {
            finishDelivery(recipe, current, output);
        }
    }

    private void finishDelivery(DeliveryRecipe recipe, ItemStack current, ItemStack output) {
        if (!current.isEmpty())
            current.grow(output.getCount());
        else {
            setItem(1, output);
        }
        progress = 0;
        this.removeItem(0, 1);
    }

    private void stopDelivery() {
        progress = 0;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[]{0, 1};
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == 1;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.javaengineering.gabazon_station");
    }


    @Override
    protected Container createMenu(int p_213906_1_, PlayerInventory p_213906_2_) {
        return null;
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return getItem(0).isEmpty() && getItem(1).isEmpty();
    }

    @Override
    public ItemStack getItem(int index) {
        return items.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int amount) {
        return ItemStackHelper.removeItem(items, index, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ItemStackHelper.takeItem(items, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        items.set(index, stack);
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return this.level != null && this.level.getBlockEntity(this.worldPosition) == this && player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ()) < 64;
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    public void load(BlockState state, CompoundNBT tags) {
        super.load(state, tags);
        this.items = NonNullList.withSize(2, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(tags, this.items);

        this.progress = tags.getInt("progress");
    }

    @Override
    public CompoundNBT save(CompoundNBT tags) {
        super.save(tags);
        ItemStackHelper.saveAllItems(tags, this.items);
        tags.putInt("progress", this.progress);
        return tags;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tags = this.getUpdateTag();
        ItemStackHelper.saveAllItems(tags, this.items);
        return new SUpdateTileEntityPacket(this.worldPosition, 1, tags);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tags = super.getUpdateTag();
        tags.putInt("progress", this.progress);
        return tags;
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (!this.remove && side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == Direction.UP)
                return this.handlers[0].cast();
            else if (side == Direction.DOWN) {
                return this.handlers[1].cast();
            } else {
                return this.handlers[2].cast();
            }
        } else {
            return super.getCapability(cap, side);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        for (LazyOptional<? extends IItemHandler> handler : this.handlers) {
            handler.invalidate();
        }
    }
}
