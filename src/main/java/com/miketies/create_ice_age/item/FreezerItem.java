package com.miketies.create_ice_age.item;

import com.miketies.create_ice_age.blaze_freezer.BlazeFreezerBlock;
import com.miketies.create_ice_age.blaze_freezer.BlazeFreezerBlockEntity;
import com.miketies.create_ice_age.block.IABlocks;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FreezerItem extends Item {
    private int freezeTime = 0;
    public FreezerItem(Properties pProperties) {
        super(pProperties);
    }

    public int getFreezeTime() {
        return this.freezeTime;
    }

    public void setFreezeTime(int freezeTime) {
        this.freezeTime = freezeTime;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        if(player == null) {
            return InteractionResult.PASS;
        }

        ItemStack itemStack = context.getItemInHand();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if(blockState.getBlock() instanceof BlazeBurnerBlock &&
                blockEntity instanceof BlazeBurnerBlockEntity bbbe) {
            // TODO: check if blaze has goggles
//            boolean goggle = bbbe.getBlockState().getValue(MAX_STACK_SIZE)
            if(!level.isClientSide()) {
//                level.setBlockAndUpdate(blockPos, Blocks.ACACIA_LOG.defaultBlockState());
                level.setBlockAndUpdate(blockPos, IABlocks.BLAZE_FREEZER.getDefaultState()
                        .setValue(BlazeFreezerBlock.FACING, level.getBlockState(blockPos).getValue(BlazeBurnerBlock.FACING))
                );
                if(level.getBlockEntity(blockPos) instanceof BlazeFreezerBlockEntity tileEntity) {
                    // TODO: Set freeze time
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
