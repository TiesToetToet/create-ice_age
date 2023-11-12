package com.miketies.create_ice_age.blaze_freezer;

import com.miketies.create_ice_age.block.IABlockEntities;
import com.miketies.create_ice_age.block.IABlocks;
import com.miketies.create_ice_age.item.IAItems;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

public class BlazeFreezerBlock extends HorizontalDirectionalBlock implements IBE<BlazeFreezerBlockEntity>, IWrenchable {
//    public static final EnumProperty<BlazeBurnerBlock.HeatLevel> HEAT_LEVEL = EnumProperty.create("blaze", BlazeBurnerBlock.HeatLevel.class);
    public static final EnumProperty<FreezingLevel> FREEZE_LEVEL = EnumProperty.create("blazee", FreezingLevel.class);
//    public static final EnumProperty<BlazeBurnerBlock.HeatLevel> HEAT_LEVEL = EnumProperty.create("blaze", BlazeBurnerBlock.HeatLevel.class);
    public BlazeFreezerBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState().setValue(FREEZE_LEVEL, FreezingLevel.NONE));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem;

        if(player.isCreative()) {
            heldItem = player.getMainHandItem().copy();
        } else {
            heldItem = player.getMainHandItem();
        }

        if(!heldItem.isEmpty()) {
            return onBlockEntityUse(level, pos, te -> {
                if(heldItem.is(IAItems.ICE_CAKE.get())) {
                    if(!level.isClientSide()) {
                        heldItem.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.PASS;
            });
        }
        return InteractionResult.PASS;
    }

    @Override
    public Class<BlazeFreezerBlockEntity> getBlockEntityClass() {
        return BlazeFreezerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BlazeFreezerBlockEntity> getBlockEntityType() {
        return IABlockEntities.BLAZE_FREEZER.get();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FREEZE_LEVEL, FACING);
    }

    @Override
    public Item asItem() {
        return AllBlocks.BLAZE_BURNER.get().asItem();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.HEATER_BLOCK_SHAPE;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        return new ItemStack(AllBlocks.BLAZE_BURNER.get());
    }

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootParams.Builder pParams) {
        var ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(AllBlocks.BLAZE_BURNER.get()));
        return ret;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return getBlockEntityType().create(pPos, pState);
    }


    public enum FreezingLevel implements StringRepresentable {
        NONE, FREEZING;

        public static FreezingLevel byIndex(int index) {
            return values()[index];
        }

        public FreezingLevel nextActiveLevel() {
            return byIndex(ordinal() % (values().length - 1) + 1);
        }

        public boolean isAtLeast(FreezingLevel freezingLevel) {
            return this.ordinal() >= freezingLevel.ordinal();
        }

        @Override
        public String getSerializedName() {
            return Lang.asId(name());
        }
    }
}
