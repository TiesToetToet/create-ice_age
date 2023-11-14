package com.miketies.create_ice_age.blaze_freezer;

import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class BlazeFreezerBlockEntity extends SmartBlockEntity {
    public static final int MAX_FREEZE_TIME = 10000;
    public static final int INSERTION_THRESHOLD = 500;
    protected int remainingFreezeTime;
    LerpedFloat headAnimation;
    LerpedFloat headAngle;
    boolean goggles;
    public BlazeFreezerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        headAnimation = LerpedFloat.linear();
        headAngle = LerpedFloat.angular();
        headAngle.startWithValue((AngleHelper
                .horizontalAngle(state.getOptionalValue(BlazeFreezerBlock.FACING)
                        .orElse(Direction.SOUTH)) + 180) % 360
        );
        goggles = false;
    }

    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide) {
            tickAnimation();
        }

        if (remainingFreezeTime > 0) {
            remainingFreezeTime--;
            level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(BlazeFreezerBlock.FREEZE_LEVEL, BlazeFreezerBlock.FreezingLevel.FREEZING));
            // TODO: random idea: Freeze nearby water
        } else {
            level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(BlazeFreezerBlock.FREEZE_LEVEL, BlazeFreezerBlock.FreezingLevel.NONE));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void tickAnimation() {
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

        headAnimation.chase(1, 0.25f, LerpedFloat.Chaser.exp(0.25f));
        headAnimation.tickChaser();
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        tag.putInt("RemainingFreezeTime", remainingFreezeTime);
        tag.putBoolean("Goggles", goggles);
        super.write(tag, clientPacket);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        remainingFreezeTime = tag.getInt("RemainingFreezeTime");
        goggles = tag.getBoolean("Goggles");
        super.read(tag, clientPacket);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    public int getRemainingFreezeTime() {
        return remainingFreezeTime;
    }

    public void setRemainingFreezeTime(int remainingFreezeTime) {
        this.remainingFreezeTime = remainingFreezeTime;
    }

    public void addRemainingFreezeTime(int remainingFreezeTime) {
        this.remainingFreezeTime += remainingFreezeTime;
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
}
