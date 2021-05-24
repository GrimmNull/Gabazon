package com.grimmyboi.javaengineering.block.monitor;


import com.grimmyboi.javaengineering.block.AbstracModtBlock;
import com.grimmyboi.javaengineering.block.fivegantenna.FiveGAntennaTileEntity;
import com.grimmyboi.javaengineering.setup.GabazonClient;
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
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("ALL")
public class MonitorBlock extends AbstracModtBlock {
    public MonitorBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MonitorTileEntity();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable IBlockReader reader, List<ITextComponent> list, ITooltipFlag flags) {
        list.add(new TranslationTextComponent("message.monitor"));
    }

    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult raytrace) {
        if (!world.isClientSide) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof MonitorTileEntity) {
                if (((MonitorTileEntity) tileEntity).energyStorage.getEnergyStored() <= 0) {
                    player.sendMessage(new TranslationTextComponent("monitor.notpowered"), UUID.randomUUID());
                    return ActionResultType.CONSUME;
                }
                if(!(world.getBlockEntity(pos.east(2)) instanceof FiveGAntennaTileEntity) &&
                        !(world.getBlockEntity(pos.south(2)) instanceof FiveGAntennaTileEntity) &&
                        !(world.getBlockEntity(pos.north(2)) instanceof FiveGAntennaTileEntity) &&
                        !(world.getBlockEntity(pos.west(2)) instanceof FiveGAntennaTileEntity)){
                    player.sendMessage(new TranslationTextComponent("antenna.notnear"), UUID.randomUUID());
                    return ActionResultType.CONSUME;
                }
                if (player.getMainHandItem().sameItem(new ItemStack(ModItems.WALLET.get()))) {
                    String[] products;
                    try {
                        products = GabazonClient.sendCommandToServer("list", 0).split(" ");
                    } catch (IOException e) {
                        products = new String[0];
                        e.printStackTrace();
                        return ActionResultType.CONSUME;
                    }
                    ItemStack tomeStack = new ItemStack(Items.WRITTEN_BOOK);
                    ListNBT bookPages = new ListNBT();
                    for (int i = 0; i < products.length / 4; i++) {
                        StringBuilder temp = new StringBuilder().append("\\nName:Quantity:Price\n")
                                .append(products[i * 4])
                                .append("\n")
                                .append(products[i * 4 + 1])
                                .append("\n")
                                .append(products[i * 4 + 2])
                                .append("\n")
                                .append(products[i * 4 + 3]);
                        bookPages.add(StringNBT.valueOf(temp.toString()));
                    }
                    tomeStack.addTagElement("pages", bookPages);
                    tomeStack.addTagElement("author", StringNBT.valueOf("GabazonStation"));
                    tomeStack.addTagElement("title", StringNBT.valueOf("Informations"));
                    world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ() + 1, tomeStack));
                    return ActionResultType.CONSUME;
                } else if(player.getMainHandItem().sameItem(new ItemStack(ModItems.AMPERMETER.get()))){
                    player.sendMessage(new TranslationTextComponent("ampermeter.voltage").append(String.valueOf(((MonitorTileEntity) tileEntity).energyStorage.getEnergyStored())), UUID.randomUUID());
                    return ActionResultType.CONSUME;
                }
                INamedContainerProvider containerProvider = new INamedContainerProvider() {
                    @Override
                    public ITextComponent getDisplayName() {
                        return new TranslationTextComponent("screen.javaengineering.monitor");
                    }

                    @Override
                    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                        return new MonitorContainer(i, world, pos, playerInventory, playerEntity);
                    }
                };
                NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, tileEntity.getBlockPos());
                return ActionResultType.CONSUME;
            } else {
                throw new IllegalStateException("Our named container provider is missing!");
            }
        }
        return ActionResultType.SUCCESS;
    }

}

