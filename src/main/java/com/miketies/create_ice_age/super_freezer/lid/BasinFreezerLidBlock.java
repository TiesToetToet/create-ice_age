package com.miketies.create_ice_age.super_freezer.lid;

import com.miketies.create_ice_age.block.IABlockEntities;
import com.miketies.create_ice_age.item.IAItems;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BasinFreezerLidBlock extends Block implements IBE<BasinFreezerLidBlockEntity> {
    public BasinFreezerLidBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Class<BasinFreezerLidBlockEntity> getBlockEntityClass() {
        return BasinFreezerLidBlockEntity.class;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.or(box(0, 0, 0, 16, 2, 16), box(6, 2, 6, 10, 3, 10));
    }


    @Override
    public BlockEntityType<? extends BasinFreezerLidBlockEntity> getBlockEntityType() {
        return IABlockEntities.BASIN_FREEZER_LID.get();
    }
}
