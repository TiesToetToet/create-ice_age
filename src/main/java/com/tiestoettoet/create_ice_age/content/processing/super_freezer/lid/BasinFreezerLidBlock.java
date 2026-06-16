//package com.tiestoettoet.create_ice_age.content.processing.super_freezer.lid;
//
//import com.simibubi.create.content.equipment.wrench.IWrenchable;
//import com.simibubi.create.foundation.block.IBE;
//import com.tiestoettoet.create_ice_age.AllBlockEntityTypes;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.world.item.context.BlockPlaceContext;
//import net.minecraft.world.level.BlockGetter;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.state.StateDefinition;
//import net.minecraft.world.level.block.state.properties.BlockStateProperties;
//import net.minecraft.world.level.block.state.properties.BooleanProperty;
//import net.minecraft.world.level.block.state.properties.DirectionProperty;
//import net.minecraft.world.phys.shapes.CollisionContext;
//import net.minecraft.world.phys.shapes.Shapes;
//import net.minecraft.world.phys.shapes.VoxelShape;
//
//public class BasinFreezerLidBlock extends Block implements IBE<BasinFreezerLidBlockEntity>, IWrenchable {
//    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
////    public static BooleanProperty OPEN = BlockStateProperties.OPEN;
//    public static final BooleanProperty OPEN = BooleanProperty.create("open");
//    private static final VoxelShape SHAPE = Shapes.or(box(0, 0, 0, 16, 2, 16), box(6, 2, 6, 10, 3, 10));
//    public BasinFreezerLidBlock(Properties pProperties) {
//        super(pProperties);
//        registerDefaultState(super.defaultBlockState().setValue(OPEN, false));
//    }
//
//    @Override
//    public Class<BasinFreezerLidBlockEntity> getBlockEntityClass() {
//        return BasinFreezerLidBlockEntity.class;
//    }
//
//    @Override
//    public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
//        return Shapes.empty();
//    }
//
//    @Override
//    protected void createBlockStateDefinition(
//            StateDefinition.Builder<Block, BlockState> builder) {
//        super.createBlockStateDefinition(builder);
//        builder.add(FACING, OPEN);
//    }
//
//    @Override
//    public BlockState getStateForPlacement(BlockPlaceContext context) {
//        return defaultBlockState()
//                .setValue(FACING, context.getHorizontalDirection().getOpposite());
//    }
//
//    @Override
//    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
////        if (!state.getValue(OPEN))
////            return SHAPE;
//        return SHAPE;
////        if (state.getValue(FACING) == Direction.SOUTH) {
////            return Shapes.or(Block.box(0, 0, 14, 16, 16, 16),
////                    Block.box(5, 5, 16, 11, 11, 18));
////        }
////        if (state.getValue(FACING) == Direction.WEST) {
////            return Shapes.or(Block.box(0, 0, 0, 2, 16, 16),
////                    Block.box(-2, 5, 5, 0, 11, 11));
////        }
////        if (state.getValue(FACING) == Direction.NORTH) {
////            return Shapes.or(Block.box(0, 0, 0, 16, 16, 2),
////                    Block.box(5, 5, -2, 11, 11, 0));
////        }
////        return Shapes.or(Block.box(14, 0, 0, 16, 16, 16),
////                Block.box(16, 5, 5, 18, 11, 11));
//    }
//
//
//    @Override
//    public BlockEntityType<? extends BasinFreezerLidBlockEntity> getBlockEntityType() {
//        return AllBlockEntityTypes.BASIN_FREEZER_LID.get();
//    }
//
//}
