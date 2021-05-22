package com.grimmyboi.javaengineering.block.monitor;


import com.grimmyboi.javaengineering.block.gabazonstation.GabazonStationContainer;
import com.grimmyboi.javaengineering.block.gabazonstation.GabazonStationTileEntity;
import com.grimmyboi.javaengineering.setup.GabazonClient;
import com.grimmyboi.javaengineering.setup.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.io.IOException;

@SuppressWarnings("ALL")
public class MonitorBlock extends Block {
    public static final DirectionProperty FACING= HorizontalBlock.FACING;
    public MonitorBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MonitorTileEntity();
    }

    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult raytrace) {
        if (!world.isClientSide) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof MonitorTileEntity) {
                if (player.getMainHandItem().sameItem(new ItemStack(ModItems.WALLET.get()))) {
                    String[] products;
                    try {
                        products= GabazonClient.sendCommandToServer("list",0).split(" ");
                    } catch (IOException e) {
                        products=new String[0];
                        e.printStackTrace();
                        return ActionResultType.CONSUME;
                    }
                    ItemStack tomeStack = new ItemStack(Items.WRITTEN_BOOK);
                    ListNBT bookPages = new ListNBT();
                    for(int i=0;i< products.length/4;i++) {
                        StringBuilder temp=new StringBuilder().append("\\nName:Quantity:Price\n")
                                                                .append(products[i*4])
                                                                .append("\n")
                                                                .append(products[i*4+1])
                                                                .append("\n")
                                                                .append(products[i*4+2])
                                                                .append("\n")
                                                                .append(products[i*4+3]);
                        bookPages.add(StringNBT.valueOf(temp.toString()));
                    }
                    tomeStack.addTagElement("pages", bookPages);
                    tomeStack.addTagElement("author", StringNBT.valueOf("GabazonStation"));
                    tomeStack.addTagElement("title", StringNBT.valueOf("Informations"));
                    world.addFreshEntity(new ItemEntity(world,pos.getX(),pos.getY(),pos.getZ()+1 ,tomeStack));
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


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return defaultBlockState().setValue(BlockStateProperties.FACING, context.getNearestLookingDirection().getOpposite()).setValue(BlockStateProperties.POWERED,false);
    }

    @Override
    public void onRemove(BlockState oldState, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if(!oldState.is(newState.getBlock())){
            TileEntity tileEntity=world.getBlockEntity(pos);
            if(tileEntity instanceof IInventory){
                InventoryHelper.dropContents(world,pos,(IInventory) tileEntity);
                world.updateNeighbourForOutputSignal(pos,this);
            }
            super.onRemove(oldState,world,pos,newState,isMoving);
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot){
        return state.setValue(FACING,rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn){
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING, BlockStateProperties.POWERED);
    }
}

