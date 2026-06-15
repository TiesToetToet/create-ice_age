package com.tiestoettoet.create_ice_age.content.processing.super_freezer.blaze_freezer;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.api.data.datamaps.BlazeBurnerFuel;
import com.simibubi.create.api.registry.CreateDataMaps;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.tiestoettoet.create_ice_age.AllParticleTypes;
import com.tiestoettoet.create_ice_age.CreateIceAge;
import com.tiestoettoet.create_ice_age.api.data.datamaps.BlazeFreezerFuel;
import com.tiestoettoet.create_ice_age.api.registry.CreateIceAgeDataMaps;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

import static com.tiestoettoet.create_ice_age.AllTags.AllItemTags.FREEZE_FUEL_REGULAR;
import static com.tiestoettoet.create_ice_age.AllTags.AllItemTags.FREEZE_FUEL_SPECIAL;
import static com.tiestoettoet.create_ice_age.content.processing.super_freezer.blaze_freezer.BlazeFreezerBlock.FREEZE_LEVEL;

public class BlazeFreezerBlockEntity extends SmartBlockEntity {
    public static final int MAX_FREEZE_TIME = 10000;
    public static final int INSERTION_THRESHOLD = 500;

    LerpedFloat headAnimation;
    public boolean isCreative;
    boolean goggles;

    protected FuelType activeFuel;
    protected int remainingFreezeTime;
    LerpedFloat headAngle;

    public BlazeFreezerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        activeFuel = FuelType.NONE;
        remainingFreezeTime = 0;
        headAnimation = LerpedFloat.linear();
        headAngle = LerpedFloat.angular();
        isCreative = false;
        goggles = false;

        headAngle.startWithValue((AngleHelper.horizontalAngle(state.getOptionalValue(BlazeBurnerBlock.FACING)
                .orElse(Direction.SOUTH)) + 180) % 360);
    }

    @Override
    public void tick() {
        super.tick();

        if (level.isClientSide) {
            if (shouldTickAnimation())
                tickAnimation();
            if (!isVirtual())
                spawnParticles(getFreezeLevelFromBlock(), 1);
            return;
        }

        if (remainingFreezeTime > 0)
            remainingFreezeTime--;

        if (activeFuel == FuelType.NORMAL)
            updateBlockState();
        if (remainingFreezeTime > 0)
            return;

        if (activeFuel == FuelType.SPECIAL) {
            activeFuel = FuelType.NORMAL;
            remainingFreezeTime = MAX_FREEZE_TIME / 2;
        } else
            activeFuel = FuelType.NONE;

        updateBlockState();
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
    }

    @OnlyIn(Dist.CLIENT)
    private boolean shouldTickAnimation() {
        // Offload the animation tick to the visual when flywheel in enabled
        return !VisualizationManager.supportsVisualization(level);
    }

    @OnlyIn(Dist.CLIENT)
    void tickAnimation() {
        boolean active = remainingFreezeTime > 0 && isValidBlockAbove();
        if (!active) {
            float target = 0;
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && !player.isInvisible()) {
                double x;
                double z;
                if (isVirtual()) {
                    x = -4;
                    z = -10;
                } else {
                    x = player.getX();
                    z = player.getZ();
                }
                double dx = x - (getBlockPos().getX() + 0.5);
                double dz = z - (getBlockPos().getZ() + 0.5);
                target = AngleHelper.deg(-Mth.atan2(dz, dx)) - 90;
            }
            target = headAngle.getValue() + AngleHelper.getShortestAngleDiff(headAngle.getValue(), target);
            headAngle.chase(target, .25f, LerpedFloat.Chaser.exp(5));
            headAngle.tickChaser();
        } else {
            headAngle.chase((AngleHelper.horizontalAngle(getBlockState().getOptionalValue(BlazeFreezerBlock.FACING)
                    .orElse(Direction.SOUTH)) + 180) % 360, .125f, LerpedFloat.Chaser.EXP);
            headAngle.tickChaser();
        }

        headAnimation.chase(active ? 1 : 0, 0.25f, LerpedFloat.Chaser.exp(0.25f));
        headAnimation.tickChaser();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        if (!isCreative) {
            compound.putInt("fuelLevel", activeFuel.ordinal());
            compound.putInt("freezeTimeRemaining", remainingFreezeTime);
        } else
            compound.putBoolean("isCreative", true);
        if (goggles)
            compound.putBoolean("Goggles", true);
        super.write(compound, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        activeFuel = FuelType.values()[compound.getInt("fuelLevel")];
        remainingFreezeTime = compound.getInt("burnTimeRemaining");
        isCreative = compound.getBoolean("isCreative");
        goggles = compound.contains("Goggles");
        super.read(compound, registries, clientPacket);
    }

    protected BlazeFreezerBlock.FreezingLevel getFreezeLevel() {
        BlazeFreezerBlock.FreezingLevel level = BlazeFreezerBlock.FreezingLevel.COOLING;
        switch (activeFuel) {
            case SPECIAL -> level = BlazeFreezerBlock.FreezingLevel.SUPER_FREEZING;
            case NORMAL -> {
                level = BlazeFreezerBlock.FreezingLevel.FREEZING;
            }
        }
        return level;
    }

    public BlazeFreezerBlock.FreezingLevel getFreezeLevelFromBlock() {
        return BlazeFreezerBlock.getFreezeLevelOf(getBlockState());
    }

    public BlazeFreezerBlock.FreezingLevel getFreezeLevelForRender() {
        return getFreezeLevelFromBlock();
    }

    public void updateBlockState() {
        setBlockFreeze(getFreezeLevel());
    }


    protected void setBlockFreeze(BlazeFreezerBlock.FreezingLevel freeze) {
        BlazeFreezerBlock.FreezingLevel inBlockState = getFreezeLevelFromBlock();
        if (inBlockState == freeze)
            return;
        level.setBlockAndUpdate(worldPosition, getBlockState().setValue(FREEZE_LEVEL, freeze));
        notifyUpdate();
    }

    protected boolean tryUpdateFuel(ItemStack itemStack, boolean forceOverflow, boolean simulate) {
        if (isCreative)
            return false;

        FuelType newFuel = FuelType.NONE;
        int newFreezeTime;

        Holder<Item> holder = itemStack.getItem().builtInRegistryHolder();
        BlazeFreezerFuel superfreezeFuel = holder.getData(CreateIceAgeDataMaps.SUPER_FREEZE_FUELS);
        BlazeFreezerFuel normalFuel = holder.getData(CreateIceAgeDataMaps.REGULAR_FREEZE_FUELS);

        // TODO: 1.21.1+ - Remove fallback to tags
        if (superfreezeFuel != null) {
            newFreezeTime = superfreezeFuel.freezeTime();
            newFuel = FuelType.SPECIAL;
        } else if (normalFuel != null) {
            newFreezeTime = normalFuel.freezeTime();
            newFuel = FuelType.NORMAL;
        } else if (FREEZE_FUEL_SPECIAL.matches(itemStack)) {
            newFreezeTime = 3200;
            newFuel = FuelType.SPECIAL;
        } else {
            newFreezeTime = itemStack.getBurnTime(null);
            if (newFreezeTime > 0) {
                newFuel = FuelType.NORMAL;
            } else if (FREEZE_FUEL_REGULAR.matches(itemStack)) {
                newFreezeTime = 1600; // Same as coal
                newFuel = FuelType.NORMAL;
            }
        }

        if (newFuel == FuelType.NONE)
            return false;
        if (newFuel.ordinal() < activeFuel.ordinal())
            return false;

        if (newFuel == activeFuel) {
            if (remainingFreezeTime <= INSERTION_THRESHOLD) {
                newFreezeTime += remainingFreezeTime;
            } else if (forceOverflow && newFuel == FuelType.NORMAL) {
                if (remainingFreezeTime < MAX_FREEZE_TIME) {
                    newFreezeTime = Math.min(remainingFreezeTime + newFreezeTime, MAX_FREEZE_TIME);
                } else {
                    newFreezeTime = remainingFreezeTime;
                }
            } else {
                return false;
            }
        }

        if (simulate)
            return true;

        activeFuel = newFuel;
        remainingFreezeTime = newFreezeTime;

        if (level.isClientSide) {
            spawnParticleBurst(activeFuel == FuelType.SPECIAL);
            return true;
        }

        BlazeFreezerBlock.FreezingLevel prev = getFreezeLevelFromBlock();
//        playSound();
        updateBlockState();

        if (prev != getFreezeLevelFromBlock())
            level.playSound(null, worldPosition, SoundEvents.BLAZE_AMBIENT, SoundSource.BLOCKS,
                    .125f + level.random.nextFloat() * .125f, 1.15f - level.random.nextFloat() * .25f);

        return true;
    }




    public boolean isValidBlockAbove() {
        if (isVirtual())
            return false;
        BlockState blockState = level.getBlockState(worldPosition.above());
        return AllBlocks.BASIN.has(blockState) || blockState.getBlock() instanceof FluidTankBlock;
    }

    public int getRemainingFreezeTime() {
        return remainingFreezeTime;
    }

    public void setRemainingFreezeTime(int remainingFreezeTime) {
        this.remainingFreezeTime = remainingFreezeTime;
    }

    public void addRemainingFreezeTime(int remainingFreezeTime) {
        CreateIceAge.LOGGER.info("Adding " + remainingFreezeTime + " to " + this.remainingFreezeTime);
        this.remainingFreezeTime += remainingFreezeTime;
    }

    protected void spawnParticles(BlazeFreezerBlock.FreezingLevel freezeLevel, double burstMult) {
        if (level == null)
            return;
        if (freezeLevel == BlazeFreezerBlock.FreezingLevel.NONE)
            return;

        RandomSource r = level.getRandom();

        Vec3 c = VecHelper.getCenterOf(worldPosition);
        Vec3 v = c.add(VecHelper.offsetRandomly(Vec3.ZERO, r, .125f)
                .multiply(1, 0, 1));

        if (r.nextInt(4) != 0)
            return;

        boolean empty = level.getBlockState(worldPosition.above())
                .getCollisionShape(level, worldPosition.above())
                .isEmpty();

        if (empty || r.nextInt(8) == 0)
            level.addParticle(ParticleTypes.SNOWFLAKE, v.x, v.y, v.z, 0, 0, 0);

        double yMotion = empty ? .0625f : r.nextDouble() * .0125f;
        Vec3 v2 = c.add(VecHelper.offsetRandomly(Vec3.ZERO, r, .5f)
                        .multiply(1, .25f, 1)
                        .normalize()
                        .scale((empty ? .25f : .5) + r.nextDouble() * .125f))
                .add(0, .5, 0);

        Vec3i v2i = new Vec3i(Mth.floor(v2.x), Mth.floor(v2.y), Mth.floor(v2.z));

        // get blockpos of v2 data
        BlockPos particlePos = new BlockPos(v2i);


        if (freezeLevel.isAtLeast(BlazeFreezerBlock.FreezingLevel.SUPER_FREEZING)) {
//            System.out.println("Spawning super freeze particle at " + v2 + " with motion " + yMotion);
            level.addParticle(new SnowSuperParticleData(), v2.x, v2.y, v2.z, 0, yMotion, 0);
        } else if (freezeLevel.isAtLeast(BlazeFreezerBlock.FreezingLevel.FREEZING))  {
//            System.out.println("Spawning freeze particle at " + v2 + " with motion " + yMotion);
            level.addParticle(new SnowParticleData(), v2.x, v2.y, v2.z, 0, yMotion, 0);
        }
        return;
    }

    public void spawnParticleBurst(boolean soulFlame) {
        Vec3 c = VecHelper.getCenterOf(worldPosition);
        RandomSource r = level.random;
        for (int i = 0; i < 20; i++) {
            Vec3 offset = VecHelper.offsetRandomly(Vec3.ZERO, r, .5f)
                    .multiply(1, .25f, 1)
                    .normalize();
            Vec3 v = c.add(offset.scale(.5 + r.nextDouble() * .125f))
                    .add(0, .125, 0);
            Vec3 m = offset.scale(1 / 32f);

            level.addParticle(soulFlame ? new SnowSuperParticleData() : new SnowParticleData(), v.x, v.y, v.z, m.x, m.y,
                    m.z);
        }
    }

    @Override
    public String toString() {
        return "BlazeFreezerBlockEntity{" +
                "remainingFreezeTime=" + remainingFreezeTime +
                ", headAnimation=" + headAnimation +
                ", headAngle=" + headAngle +
                ", goggles=" + goggles +
                '}';
    }

    public enum FuelType {
        NONE, NORMAL, SPECIAL
    }
}
