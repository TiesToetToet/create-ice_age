package com.tiestoettoet.create_ice_age;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.blaze_freezer.BlazeFreezerBlockEntity;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.blaze_freezer.BlazeFreezerRenderer;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.blaze_freezer.BlazeFreezerVisual;
//import com.tiestoettoet.create_ice_age.content.processing.super_freezer.lid.BasinFreezerLidBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class AllBlockEntityTypes {
    private static final CreateRegistrate REGISTRATE = CreateIceAge.registrate();

    public static final BlockEntityEntry<BlazeFreezerBlockEntity> BLAZE_FREEZER = REGISTRATE
            .blockEntity("blaze_freezer", BlazeFreezerBlockEntity::new)
            .visual(() -> BlazeFreezerVisual::new, false)
            .validBlocks(AllBlocks.BLAZE_FREEZER)
            .renderer(() -> BlazeFreezerRenderer::new)
            .register();

//    public static final BlockEntityEntry<BasinFreezerLidBlockEntity> BASIN_FREEZER_LID = REGISTRATE
//            .blockEntity("basin_freezer_lid", BasinFreezerLidBlockEntity::new)
//            .validBlocks(AllBlocks.BASIN_FREEZER_LID)
//            .register();

    public static void register() {

    }
}
