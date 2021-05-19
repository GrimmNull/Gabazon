package com.grimmyboi.javaengineering.block.fivegantenna;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

@SuppressWarnings("ALL")
public class FiveGAntennaBlock extends Block {
    public static final DirectionProperty FACING= HorizontalBlock.FACING;
    public FiveGAntennaBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new FiveGAntennaTileEntity();
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult raytrace) {
        if(world.isClientSide){
            return ActionResultType.SUCCESS;
        }
        this.interactWith(world,pos,player);
        return ActionResultType.CONSUME;
    }

    private void interactWith(World world, BlockPos pos, PlayerEntity player){
        TileEntity tileEntity=world.getBlockEntity(pos);
        if(tileEntity instanceof FiveGAntennaTileEntity && player instanceof ServerPlayerEntity){
            FiveGAntennaTileEntity tg = (FiveGAntennaTileEntity) tileEntity;
            NetworkHooks.openGui((ServerPlayerEntity) player,tg,tg::encodeExtraData);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING,context.getHorizontalDirection().getOpposite());
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
    protected void createBlockStateDefinition(StateContainer.Builder<Block,BlockState> builder){
        builder.add(FACING);
    }
}

