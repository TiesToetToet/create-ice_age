package com.tiestoettoet.create_ice_age.content.processing.basin;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.blaze_freezer.BlazeFreezerBlock;

public interface CreateIceAgeBasinBlockEntityExtension {
    BlazeFreezerBlock.FreezingLevel iceAge$getFreezeLevel();

    BlazeBurnerBlock.HeatLevel iceAge$getHeatLevel();
}
