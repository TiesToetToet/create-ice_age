package com.tiestoettoet.create_ice_age.content.processing.super_freezer.breeze_freezer;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllShapes;
import com.simibubi.create.api.schematic.requirement.SpecialBlockEntityItemRequirement;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.block.IBE;
import com.tiestoettoet.create_ice_age.AllBlockEntityTypes;
import com.tiestoettoet.create_ice_age.AllBlocks;
import net.createmod.catnip.lang.Lang;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.util.FakePlayer;


public class BreezeFreezerBlock extends HorizontalDirectionalBlock implements IBE<BreezeFreezerBlockEntity>, IWrenchable, SpecialBlockEntityItemRequirement {
//    public static final EnumProperty<BlazeBurnerBlock.HeatLevel> HEAT_LEVEL = EnumProperty.create("blaze", BlazeBurnerBlock.HeatLevel.class);
    public static final EnumProperty<FreezingLevel> FREEZE_LEVEL = EnumProperty.create("breeze", FreezingLevel.class);
//    public static final EnumProperty<BlazeBurnerBlock.HeatLevel> HEAT_LEVEL = EnumProperty.create("blaze", BlazeBurnerBlock.HeatLevel.class);

    public static final MapCodec<BreezeFreezerBlock> CODEC = simpleCodec(BreezeFreezerBlock::new);

    public BreezeFreezerBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FREEZE_LEVEL, FreezingLevel.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FREEZE_LEVEL, FACING);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (level.isClientSide) {
            return;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos.above());
        if (!(blockEntity instanceof BasinBlockEntity basinBlockEntity)) {
            return;
        }
        basinBlockEntity.notifyChangeOfContents();
    }


    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return getLitOrUnlitStack(state);
    }


    @Override
    public Class<BreezeFreezerBlockEntity> getBlockEntityClass() {
        return BreezeFreezerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BreezeFreezerBlockEntity> getBlockEntityType() {
        return AllBlockEntityTypes.BREEZE_FREEZER.get();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (state.getValue(FREEZE_LEVEL) == FreezingLevel.NONE)
            return null;
        return IBE.super.newBlockEntity(pos, state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        FreezingLevel freeze = state.getValue(FREEZE_LEVEL);

        if (AllItems.GOGGLES.isIn(stack) && freeze != FreezingLevel.NONE)
            return onBlockEntityUseItemOn(level, pos, bbte -> {
                if (bbte.goggles)
                    return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                bbte.goggles = true;
                bbte.notifyUpdate();
                return ItemInteractionResult.SUCCESS;
            });

        BreezeFreezerBlockEntity be = getBlockEntity(level, pos);

        if (stack.isEmpty() && freeze != FreezingLevel.NONE)
            return onBlockEntityUseItemOn(level, pos, bbte -> {
                if (!bbte.goggles)
                    return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                bbte.goggles = false;
                bbte.notifyUpdate();
                return ItemInteractionResult.SUCCESS;
            });

        boolean doNotConsume = player.isCreative();
        boolean forceOverflow = !(player instanceof FakePlayer);

        InteractionResultHolder<ItemStack> res =
                tryInsert(state, level, pos, stack, doNotConsume, forceOverflow, false);
        ItemStack leftover = res.getObject();
        if (!level.isClientSide && !doNotConsume && !leftover.isEmpty()) {
            if (stack.isEmpty()) {
                player.setItemInHand(hand, leftover);
            } else if (!player.getInventory()
                    .add(leftover)) {
                player.drop(leftover, false);
            }
        }

        return res.getResult() == InteractionResult.SUCCESS ? ItemInteractionResult.SUCCESS : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public static InteractionResultHolder<ItemStack> tryInsert(BlockState state, Level world, BlockPos pos,
                                                               ItemStack stack, boolean doNotConsume, boolean forceOverflow, boolean simulate) {
        if (!state.hasBlockEntity())
            return InteractionResultHolder.fail(ItemStack.EMPTY);

        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof BreezeFreezerBlockEntity freezerBE))
            return InteractionResultHolder.fail(ItemStack.EMPTY);

//        if (freezerBE.isCreativeFuel(stack)) {
//            if (!simulate)
//                freezerBE.applyCreativeFuel();
//            return InteractionResultHolder.success(ItemStack.EMPTY);
//        }
        if (!freezerBE.tryUpdateFuel(stack, forceOverflow, simulate))
            return InteractionResultHolder.fail(ItemStack.EMPTY);

        if (!doNotConsume) {
            ItemStack container = stack.hasCraftingRemainingItem() ? stack.getCraftingRemainingItem() : ItemStack.EMPTY;
            if (!world.isClientSide) {
                stack.shrink(1);
            }
            return InteractionResultHolder.success(container);
        }
        return InteractionResultHolder.success(ItemStack.EMPTY);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        ItemStack stack = context.getItemInHand();
        Item item = stack.getItem();
        BlockState defaultState = defaultBlockState();
        if (!(item instanceof BreezeFreezerBlockItem))
            return defaultState;
        FreezingLevel initialFreeze =
                ((BreezeFreezerBlockItem) item).hasCapturedBreeze() ? FreezingLevel.COOLING : FreezingLevel.NONE;
        return defaultState.setValue(FREEZE_LEVEL, initialFreeze)
                .setValue(FACING, context.getHorizontalDirection()
                        .getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.HEATER_BLOCK_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_220071_1_, BlockGetter p_220071_2_, BlockPos p_220071_3_,
                                        CollisionContext p_220071_4_) {
        if (p_220071_4_ == CollisionContext.empty())
            return AllShapes.HEATER_BLOCK_SPECIAL_COLLISION_SHAPE;
        return getShape(p_220071_1_, p_220071_2_, p_220071_3_, p_220071_4_);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState p_149740_1_) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level p_180641_2_, BlockPos p_180641_3_) {
        return Math.max(0, state.getValue(FREEZE_LEVEL)
                .ordinal() - 1);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (random.nextInt(10) != 0)
            return;
        if (!state.getValue(FREEZE_LEVEL)
                .isAtLeast(FreezingLevel.COOLING))
            return;
//        world.playLocalSound((double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F),
//                (double) ((float) pos.getZ() + 0.5F), SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS,
//                0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.6F, false);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return null;
    }

    public static FreezingLevel getFreezeLevelOf(BlockState blockState) {
        return blockState.hasProperty(FREEZE_LEVEL) ? blockState.getValue(FREEZE_LEVEL)
                : FreezingLevel.NONE;
    }

    public static FreezingLevel getFreezeLevel(BlockState state) {
        return state.hasProperty(FREEZE_LEVEL) ? state.getValue(FREEZE_LEVEL) : FreezingLevel.NONE;
    }

    public static LootTable.Builder buildLootTable() {
        LootItemCondition.Builder survivesExplosion = ExplosionCondition.survivesExplosion();
        BreezeFreezerBlock block = AllBlocks.BREEZE_FREEZER.get();
        LootTable.Builder builder = LootTable.lootTable();
        LootPool.Builder poolBuilder = LootPool.lootPool();
        for (FreezingLevel level : FreezingLevel.values()) {
            ItemLike drop = level == FreezingLevel.NONE ? com.simibubi.create.AllItems.EMPTY_BLAZE_BURNER.get() : AllBlocks.BREEZE_FREEZER.get();
            poolBuilder.add(LootItem.lootTableItem(drop)
                    .when(survivesExplosion)
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                    .hasProperty(FREEZE_LEVEL, level))));
        }
        builder.withPool(poolBuilder.setRolls(ConstantValue.exactly(1)));
        return builder;
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, getLitOrUnlitStack(state));
    }

    private static ItemStack getLitOrUnlitStack(BlockState state) {
        boolean isLit = state.getValue(FREEZE_LEVEL) != FreezingLevel.NONE;
        return (isLit ? AllBlocks.BREEZE_FREEZER : com.simibubi.create.AllItems.EMPTY_BLAZE_BURNER).asStack();
    }


    public enum FreezingLevel implements StringRepresentable {
        NONE, COOLING, FREEZING, SUPER_FREEZING;

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
