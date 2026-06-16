package com.tiestoettoet.create_ice_age;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class AllPartialModels {

    public static final PartialModel
            BREEZE_CAGE = block("breeze_freezer/block"),
            FREEZE_INERT = block("breeze_freezer/freeze/inert"), FREEZE_SUPER_ACTIVE = block("breeze_freezer/freeze/super_active"),
            BREEZE_GOGGLES = block("breeze_freezer/goggles"), BREEZE_GOGGLES_SMALL = block("breeze_freezer/goggles_small"),
            FREEZE_IDLE = block("breeze_freezer/freeze/idle"), FREEZE_ACTIVE = block("breeze_freezer/freeze/active"),
            FREEZE_SUPER = block("breeze_freezer/freeze/super"), BREEZE_FREEZER_FLAME = block("breeze_freezer/flame"),
            BREEZE_FREEZER_RODS = block("breeze_freezer/rods_small"), BREEZE_FREEZER_RODS_2 = block("breeze_freezer/rods_large"),
            BREEZE_FREEZER_SUPER_RODS = block("breeze_freezer/superheated_rods_small"),
            BREEZE_FREEZER_SUPER_RODS_2 = block("breeze_freezer/superheated_rods_large");

    private static PartialModel block(String path) {
//        System.out.println(Create.asResource("block/" + path));
//        System.out.println(CreateIceAge.asResource("block/" + path));
        return PartialModel.of(CreateIceAge.asResource("block/" + path));
    }

    public static void init() {
        //
    }
}
