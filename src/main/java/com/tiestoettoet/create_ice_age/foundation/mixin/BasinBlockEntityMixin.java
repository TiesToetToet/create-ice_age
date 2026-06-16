package com.tiestoettoet.create_ice_age.foundation.mixin;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.tiestoettoet.create_ice_age.content.processing.basin.CreateIceAgeBasinBlockEntity;
import com.tiestoettoet.create_ice_age.content.processing.basin.CreateIceAgeBasinBlockEntityExtension;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.breeze_freezer.BreezeFreezerBlock;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BasinBlockEntity.class)
public abstract class BasinBlockEntityMixin
        implements CreateIceAgeBasinBlockEntityExtension {

    @Shadow
    private @Nullable BlazeBurnerBlock.HeatLevel cachedHeatLevel;

    @Unique
    private BreezeFreezerBlock.FreezingLevel iceAge$cachedFreezeLevel;

    @Inject(method = "tick", at = @At("HEAD"))
    private void iceAge$clearFreezeCache(CallbackInfo ci) {
        iceAge$cachedFreezeLevel = null;
    }

    @Override
    public BlazeBurnerBlock.HeatLevel iceAge$getHeatLevel() {
        BasinBlockEntity basin = (BasinBlockEntity)(Object)this;

        if (cachedHeatLevel == null) {
            if (basin.getLevel() == null)
                return BlazeBurnerBlock.HeatLevel.NONE;

            cachedHeatLevel =
                    CreateIceAgeBasinBlockEntity.getHeatLevelOf(
                            basin.getLevel().getBlockState(
                                    basin.getBlockPos().below()
                            )
                    );
        }

        return cachedHeatLevel;
    }

    @Override
    public BreezeFreezerBlock.FreezingLevel iceAge$getFreezeLevel() {
        BasinBlockEntity basin = (BasinBlockEntity)(Object)this;

        if (iceAge$cachedFreezeLevel == null) {
            if (basin.getLevel() == null)
                return BreezeFreezerBlock.FreezingLevel.NONE;

            iceAge$cachedFreezeLevel =
                    CreateIceAgeBasinBlockEntity.getFreezeLevelOf(
                            basin.getLevel().getBlockState(
                                    basin.getBlockPos().below()
                            )
                    );
        }

        return iceAge$cachedFreezeLevel;
    }
}