package com.grimmyboi.javaengineering.block.gabazonstation;


import com.grimmyboi.javaengineering.block.AbstracModtBlock;
import com.grimmyboi.javaengineering.setup.Config;
import com.grimmyboi.javaengineering.setup.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("ALL")
public class GabazonStationBlock extends AbstracModtBlock {

    public GabazonStationBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable IBlockReader reader, List<ITextComponent> list, ITooltipFlag flags) {
        list.add(new TranslationTextComponent("message.station", Integer.toString(Config.GABAZONSTATION_RECEIVE.get())));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new GabazonStationTileEntity();
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult raytrace) {
        if (!world.isClientSide) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof GabazonStationTileEntity) {
                if(((GabazonStationTileEntity) tileEntity).energyStorage.getEnergyStored()<=0){
                    player.sendMessage(new TranslationTextComponent("station.notpowered"), UUID.randomUUID());
                    return ActionResultType.CONSUME;
                }
                if (player.getMainHandItem().sameItem(new ItemStack(ModItems.WALLET.get()))) {
                    INamedContainerProvider containerProvider = new INamedContainerProvider() {
                        @Override
                        public ITextComponent getDisplayName() {
                            return new TranslationTextComponent("screen.javaengineering.station");
                        }

                        @Override
                        public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                            return new GabazonStationContainer(i, world, pos, playerInventory, playerEntity);
                        }
                    };
                    NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, tileEntity.getBlockPos());
                    return ActionResultType.CONSUME;
                } else if (player.getMainHandItem().sameItem(new ItemStack(ModItems.ENGINEER_KEY.get()))) {
                    ItemStack tomeStack = new ItemStack(Items.WRITTEN_BOOK);
                    ListNBT bookPages = new ListNBT();
                    bookPages.add(StringNBT.valueOf("\\nTap the monitor with your wallet to get the list of products"));
                    bookPages.add(StringNBT.valueOf("\\nTo buy you have to write a book with the following format:\nbuy: \nQUANTITY1xITEM1\nQUANTITY2xITEM2\n..."));
                    tomeStack.addTagElement("pages", bookPages);
                    tomeStack.addTagElement("author", StringNBT.valueOf("GabazonStation"));
                    tomeStack.addTagElement("title", StringNBT.valueOf("Informations"));
                    world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ() + 1, tomeStack));
                }
            } else {
                throw new IllegalStateException("Our named container provider is missing!");
            }
        }
        return ActionResultType.SUCCESS;
    }

}

