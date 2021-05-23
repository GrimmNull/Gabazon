package com.grimmyboi.javaengineering.block.energypipe;


import com.grimmyboi.javaengineering.block.AbstracModtBlock;
import com.grimmyboi.javaengineering.block.gabazonstation.GabazonStationTileEntity;
import com.grimmyboi.javaengineering.block.thermalgenerator.ThermalGeneratorTileEntity;
import com.grimmyboi.javaengineering.setup.Config;
import com.grimmyboi.javaengineering.setup.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
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

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("ALL")
public class EnergyPipeBlock extends AbstracModtBlock {
    public EnergyPipeBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnergyPipeTileEntity();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable IBlockReader reader, List<ITextComponent> list, ITooltipFlag flags) {
        list.add(new TranslationTextComponent("message.pipe"));
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult raytrace) {
        if (world != null && world.isClientSide) {
            return ActionResultType.SUCCESS;
        }
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof EnergyPipeTileEntity) {
            if (player.getMainHandItem().sameItem(new ItemStack(ModItems.AMPERMETER.get()))) {
                player.sendMessage(new TranslationTextComponent("ampermeter.voltage").append(String.valueOf(((EnergyPipeTileEntity) tileEntity).energyStorage.getEnergyStored())), UUID.randomUUID());
                return ActionResultType.CONSUME;
            }
        }
        return ActionResultType.SUCCESS;
    }


}

