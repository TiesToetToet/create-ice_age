package com.tiestoettoet.create_ice_age;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class AllPartialModels {

    public static final PartialModel
            BLAZE_CAGE = block("blaze_freezer/block"),
            FREEZE_INERT = block("blaze_freezer/freeze/inert"), FREEZE_SUPER_ACTIVE = block("blaze_freezer/freeze/super_active"),
            BLAZE_GOGGLES = block("blaze_freezer/goggles"), BLAZE_GOGGLES_SMALL = block("blaze_freezer/goggles_small"),
            FREEZE_IDLE = block("blaze_freezer/freeze/idle"), FREEZE_ACTIVE = block("blaze_freezer/freeze/active"),
            FREEZE_SUPER = block("blaze_freezer/freeze/super"), BLAZE_FREEZER_FLAME = block("blaze_freezer/flame"),
            BLAZE_FREEZER_RODS = block("blaze_freezer/rods_small"), BLAZE_FREEZER_RODS_2 = block("blaze_freezer/rods_large"),
            BLAZE_FREEZER_SUPER_RODS = block("blaze_freezer/superheated_rods_small"),
            BLAZE_FREEZER_SUPER_RODS_2 = block("blaze_freezer/superheated_rods_large");

    private static PartialModel block(String path) {
//        System.out.println(Create.asResource("block/" + path));
//        System.out.println(CreateIceAge.asResource("block/" + path));
        return PartialModel.of(CreateIceAge.asResource("block/" + path));
    }

    public static void init() {
        //
    }
}
