package com.miketies.create_ice_age.block;

import com.miketies.create_ice_age.super_freezer.blaze_freezer.BlazeFreezerBlockEntity;
import com.miketies.create_ice_age.super_freezer.lid.BasinFreezerLidBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.miketies.create_ice_age.super_freezer.blaze_freezer.BlazeFreezerRenderer;

import static com.miketies.create_ice_age.CreateIceAge.ICE_AGE_REGISTRATE;

public class IABlockEntities {
    public static final BlockEntityEntry<BlazeFreezerBlockEntity> BLAZE_FREEZER = ICE_AGE_REGISTRATE
            .blockEntity("blaze_freezer", BlazeFreezerBlockEntity::new)
            .validBlocks(IABlocks.BLAZE_FREEZER)
            .renderer(() -> BlazeFreezerRenderer::new)
            .register();

    public static final BlockEntityEntry<BasinFreezerLidBlockEntity> BASIN_FREEZER_LID = ICE_AGE_REGISTRATE
            .blockEntity("basin_freezer_lid", BasinFreezerLidBlockEntity::new)
            .validBlocks(IABlocks.BASIN_FREEZER_LID)
            .register();

    public static void register() {}
}
