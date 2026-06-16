package com.tiestoettoet.create_ice_age.content.processing.basin;

import com.simibubi.create.AllParticleTypes;
import com.simibubi.create.content.fluids.FluidFX;
import com.simibubi.create.content.fluids.particle.FluidParticleData;
import com.simibubi.create.content.processing.basin.BasinBlock;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinInventory;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.InvManipulationBehaviour;
import com.simibubi.create.foundation.item.SmartInventory;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.breeze_freezer.BreezeFreezerBlock;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.data.IntAttached;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.breeze_freezer.BreezeFreezerBlock.FreezingLevel;

import java.util.*;

public class CreateIceAgeBasinBlockEntity extends BasinBlockEntity {
    private boolean areFluidsMoving;
    LerpedFloat ingredientRotationSpeed;
    LerpedFloat ingredientRotation;

    public BasinInventory inputInventory;
    public SmartFluidTankBehaviour inputTank;
    protected SmartInventory outputInventory;
    protected SmartFluidTankBehaviour outputTank;
    private FilteringBehaviour filtering;
    private boolean contentsChanged;

    private Couple<SmartInventory> invs;
    private Couple<SmartFluidTankBehaviour> tanks;

    protected IItemHandlerModifiable itemCapability;
    protected IFluidHandler fluidCapability;

    List<Direction> disabledSpoutputs;
    Direction preferredSpoutput;
    protected List<ItemStack> spoutputBuffer;
    protected List<FluidStack> spoutputFluidBuffer;
    int recipeBackupCheck;

    public static final int OUTPUT_ANIMATION_TIME = 10;
    List<IntAttached<ItemStack>> visualizedOutputItems;
    List<IntAttached<FluidStack>> visualizedOutputFluids;

    private @Nullable BlazeBurnerBlock.HeatLevel cachedHeatLevel;
    private @Nullable FreezingLevel cachedFreezeLevel;


    public CreateIceAgeBasinBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inputInventory = new BasinInventory(9, this);
        inputInventory.whenContentsChanged($ -> contentsChanged = true);
        outputInventory = new BasinInventory(9, this).forbidInsertion()
                .withMaxStackSize(64);
        areFluidsMoving = false;
        itemCapability = new CombinedInvWrapper(inputInventory, outputInventory);
        contentsChanged = true;
        ingredientRotation = LerpedFloat.angular()
                .startWithValue(0);
        ingredientRotationSpeed = LerpedFloat.linear()
                .startWithValue(0);

        invs = Couple.create(inputInventory, outputInventory);
        tanks = Couple.create(inputTank, outputTank);
        visualizedOutputItems = Collections.synchronizedList(new ArrayList<>());
        visualizedOutputFluids = Collections.synchronizedList(new ArrayList<>());
        disabledSpoutputs = new ArrayList<>();
        preferredSpoutput = null;
        spoutputBuffer = new ArrayList<>();
        spoutputFluidBuffer = new ArrayList<>();
        recipeBackupCheck = 20;
    }

    @Override
    public void tick() {
        cachedHeatLevel = null;
        cachedFreezeLevel = null;

        super.tick();
        if (level.isClientSide) {
            createFluidParticles();
            tickVisualizedOutputs();
            ingredientRotationSpeed.tickChaser();
            ingredientRotation.setValue(ingredientRotation.getValue() + ingredientRotationSpeed.getValue());
        }

        if ((!spoutputBuffer.isEmpty() || !spoutputFluidBuffer.isEmpty()) && !level.isClientSide)
            tryClearingSpoutputOverflow();
        if (!contentsChanged)
            return;

        contentsChanged = false;
        getOperator().ifPresent(be -> be.basinChecker.scheduleUpdate());

        for (Direction offset : Iterate.horizontalDirections) {
            BlockPos toUpdate = worldPosition.above()
                    .relative(offset);
            BlockState stateToUpdate = level.getBlockState(toUpdate);
            if (stateToUpdate.getBlock() instanceof BasinBlock
                    && stateToUpdate.getValue(BasinBlock.FACING) == offset.getOpposite()) {
                BlockEntity be = level.getBlockEntity(toUpdate);
                if (be instanceof CreateIceAgeBasinBlockEntity)
                    ((CreateIceAgeBasinBlockEntity) be).contentsChanged = true;
            }
        }
    }

    private Optional<BasinOperatingBlockEntity> getOperator() {
        if (level == null)
            return Optional.empty();
        BlockEntity be = level.getBlockEntity(worldPosition.above(2));
        if (be instanceof BasinOperatingBlockEntity)
            return Optional.of((BasinOperatingBlockEntity) be);
        return Optional.empty();
    }

    private void tryClearingSpoutputOverflow() {
        BlockState blockState = getBlockState();
        if (!(blockState.getBlock() instanceof BasinBlock))
            return;
        Direction direction = blockState.getValue(BasinBlock.FACING);
        BlockEntity be = level.getBlockEntity(worldPosition.below()
                .relative(direction));

        FilteringBehaviour filter = null;
        InvManipulationBehaviour inserter = null;
        if (be != null) {
            filter = BlockEntityBehaviour.get(level, be.getBlockPos(), FilteringBehaviour.TYPE);
            inserter = BlockEntityBehaviour.get(level, be.getBlockPos(), InvManipulationBehaviour.TYPE);
        }

        if (filter != null && filter.isRecipeFilter())
            filter = null; // Do not test spout outputs against the recipe filter

        IItemHandler targetInv = be == null ? null
                : Optional.ofNullable(level.getCapability(Capabilities.ItemHandler.BLOCK, be.getBlockPos(), direction.getOpposite()))
                .orElse(inserter == null ? null : inserter.getInventory());

        IFluidHandler targetTank = be == null ? null
                : level.getCapability(Capabilities.FluidHandler.BLOCK, be.getBlockPos(), direction.getOpposite());

        boolean update = false;

        for (Iterator<ItemStack> iterator = spoutputBuffer.iterator(); iterator.hasNext(); ) {
            ItemStack itemStack = iterator.next();

            if (direction == Direction.DOWN) {
                Block.popResource(level, worldPosition, itemStack);
                iterator.remove();
                update = true;
                continue;
            }

            if (targetInv == null)
                break;

            ItemStack remainder = ItemHandlerHelper.insertItemStacked(targetInv, itemStack, true);
            if (remainder.getCount() == itemStack.getCount())
                continue;
            if (filter != null && !filter.test(itemStack))
                continue;

            if (visualizedOutputItems.size() < 3)
                visualizedOutputItems.add(IntAttached.withZero(itemStack));
            update = true;

            remainder = ItemHandlerHelper.insertItemStacked(targetInv, itemStack.copy(), false);
            if (remainder.isEmpty())
                iterator.remove();
            else
                itemStack.setCount(remainder.getCount());
        }

        for (Iterator<FluidStack> iterator = spoutputFluidBuffer.iterator(); iterator.hasNext(); ) {
            FluidStack fluidStack = iterator.next();

            if (direction == Direction.DOWN) {
                iterator.remove();
                update = true;
                continue;
            }

            if (targetTank == null)
                break;

            for (boolean simulate : Iterate.trueAndFalse) {
                IFluidHandler.FluidAction action = simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
                int fill = targetTank instanceof SmartFluidTankBehaviour.InternalFluidHandler
                        ? ((SmartFluidTankBehaviour.InternalFluidHandler) targetTank).forceFill(fluidStack.copy(), action)
                        : targetTank.fill(fluidStack.copy(), action);
                if (fill != fluidStack.getAmount())
                    break;
                if (simulate)
                    continue;

                update = true;
                iterator.remove();
                if (visualizedOutputFluids.size() < 3)
                    visualizedOutputFluids.add(IntAttached.withZero(fluidStack));
            }
        }

        if (update) {
            notifyChangeOfContents();
            sendData();
        }
    }

    public static FreezingLevel getFreezeLevelOf(BlockState state) {
        if (state.hasProperty(BreezeFreezerBlock.FREEZE_LEVEL))
            return state.getValue(BreezeFreezerBlock.FREEZE_LEVEL);
        return FreezingLevel.NONE;
    }

    private void tickVisualizedOutputs() {
        visualizedOutputFluids.forEach(IntAttached::decrement);
        visualizedOutputItems.forEach(IntAttached::decrement);
        visualizedOutputFluids.removeIf(IntAttached::isOrBelowZero);
        visualizedOutputItems.removeIf(IntAttached::isOrBelowZero);
    }

    private void createFluidParticles() {
        RandomSource r = level.random;

        if (!visualizedOutputFluids.isEmpty())
            createOutputFluidParticles(r);

        if (!areFluidsMoving && r.nextFloat() > 1 / 8f)
            return;

        int segments = 0;
        for (SmartFluidTankBehaviour behaviour : getTanks()) {
            if (behaviour == null)
                continue;
            for (SmartFluidTankBehaviour.TankSegment tankSegment : behaviour.getTanks())
                if (!tankSegment.isEmpty(0))
                    segments++;
        }
        if (segments < 2)
            return;

        float totalUnits = getTotalFluidUnits(0);
        if (totalUnits == 0)
            return;
        float fluidLevel = Mth.clamp(totalUnits / 2000, 0, 1);
        float rim = 2 / 16f;
        float space = 12 / 16f;
        float surface = worldPosition.getY() + rim + space * fluidLevel + 1 / 32f;

        if (areFluidsMoving) {
            createMovingFluidParticles(surface, segments);
            return;
        }

        for (SmartFluidTankBehaviour behaviour : getTanks()) {
            if (behaviour == null)
                continue;
            for (SmartFluidTankBehaviour.TankSegment tankSegment : behaviour.getTanks()) {
                if (tankSegment.isEmpty(0))
                    continue;
                float x = worldPosition.getX() + rim + space * r.nextFloat();
                float z = worldPosition.getZ() + rim + space * r.nextFloat();
                level.addAlwaysVisibleParticle(
                        new FluidParticleData(AllParticleTypes.BASIN_FLUID.get(), tankSegment.getRenderedFluid()), x,
                        surface, z, 0, 0, 0);
            }
        }
    }

    private void createOutputFluidParticles(RandomSource r) {
        BlockState blockState = getBlockState();
        if (!(blockState.getBlock() instanceof BasinBlock))
            return;
        Direction direction = blockState.getValue(BasinBlock.FACING);
        if (direction == Direction.DOWN)
            return;
        Vec3 directionVec = Vec3.atLowerCornerOf(direction.getNormal());
        Vec3 outVec = VecHelper.getCenterOf(worldPosition)
                .add(directionVec.scale(.65)
                        .subtract(0, 1 / 4f, 0));
        Vec3 outMotion = directionVec.scale(1 / 16f)
                .add(0, -1 / 16f, 0);

        for (int i = 0; i < 2; i++) {
            visualizedOutputFluids.forEach(ia -> {
                FluidStack fluidStack = ia.getValue();
                ParticleOptions fluidParticle = FluidFX.getFluidParticle(fluidStack);
                Vec3 m = VecHelper.offsetRandomly(outMotion, r, 1 / 16f);
                level.addAlwaysVisibleParticle(fluidParticle, outVec.x, outVec.y, outVec.z, m.x, m.y, m.z);
            });
        }
    }

    private void createMovingFluidParticles(float surface, int segments) {
        Vec3 pointer = new Vec3(1, 0, 0).scale(1 / 16f);
        float interval = 360f / segments;
        Vec3 centerOf = VecHelper.getCenterOf(worldPosition);
        float intervalOffset = (AnimationTickHolder.getTicks() * 18) % 360;

        int currentSegment = 0;
        for (SmartFluidTankBehaviour behaviour : getTanks()) {
            if (behaviour == null)
                continue;
            for (SmartFluidTankBehaviour.TankSegment tankSegment : behaviour.getTanks()) {
                if (tankSegment.isEmpty(0))
                    continue;
                float angle = interval * (1 + currentSegment) + intervalOffset;
                Vec3 vec = centerOf.add(VecHelper.rotate(pointer, angle, Direction.Axis.Y));
                level.addAlwaysVisibleParticle(
                        new FluidParticleData(AllParticleTypes.BASIN_FLUID.get(), tankSegment.getRenderedFluid()), vec.x(),
                        surface, vec.z(), 1, 0, 0);
                currentSegment++;
            }
        }
    }

    @NotNull BlazeBurnerBlock.HeatLevel getHeatLevel() {
        if (cachedHeatLevel == null) {
            if (level == null)
                return BlazeBurnerBlock.HeatLevel.NONE;

            cachedHeatLevel = getHeatLevelOf(level.getBlockState(getBlockPos().below(1)));
        }
        return cachedHeatLevel;
    }

    @NotNull FreezingLevel getFreezeLevel() {
        if (cachedFreezeLevel == null) {
            if (level == null)
                return FreezingLevel.NONE;

            cachedFreezeLevel = getFreezeLevelOf(level.getBlockState(getBlockPos().below(1)));
        }
        return cachedFreezeLevel;
    }
}
