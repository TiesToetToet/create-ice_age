package com.tiestoettoet.create_ice_age.content.processing.basin;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.breeze_freezer.BreezeFreezerBlock;

public interface CreateIceAgeBasinBlockEntityExtension {
    BreezeFreezerBlock.FreezingLevel iceAge$getFreezeLevel();

    BlazeBurnerBlock.HeatLevel iceAge$getHeatLevel();
}
