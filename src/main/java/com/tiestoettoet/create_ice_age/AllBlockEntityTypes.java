package com.tiestoettoet.create_ice_age;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.breeze_freezer.BreezeFreezerBlockEntity;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.breeze_freezer.BreezeFreezerRenderer;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.breeze_freezer.BreezeFreezerVisual;
//import com.tiestoettoet.create_ice_age.content.processing.super_freezer.lid.BasinFreezerLidBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class AllBlockEntityTypes {
    private static final CreateRegistrate REGISTRATE = CreateIceAge.registrate();

    public static final BlockEntityEntry<BreezeFreezerBlockEntity> BREEZE_FREEZER = REGISTRATE
            .blockEntity("breeze_freezer", BreezeFreezerBlockEntity::new)
            .visual(() -> BreezeFreezerVisual::new, false)
            .validBlocks(AllBlocks.BREEZE_FREEZER)
            .renderer(() -> BreezeFreezerRenderer::new)
            .register();

//    public static final BlockEntityEntry<BasinFreezerLidBlockEntity> BASIN_FREEZER_LID = REGISTRATE
//            .blockEntity("basin_freezer_lid", BasinFreezerLidBlockEntity::new)
//            .validBlocks(AllBlocks.BASIN_FREEZER_LID)
//            .register();

    public static void register() {

    }
}
