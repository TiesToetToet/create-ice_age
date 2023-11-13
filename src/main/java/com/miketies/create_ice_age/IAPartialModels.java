package com.miketies.create_ice_age;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.Create;

public class IAPartialModels {
    public static final PartialModel

    BLAZE_FREEZER_HEAD = block("blaze_freezer_head"),
    BLAZE_FREEZER_RODS_BIG = block("frozen_rods_large"),
    BLAZE_FREEZER_RODS_SMALL = block("frozen_rods_small");
    private static PartialModel block(String path) {
        System.out.println("IAPartialModels.block: " + path + " " + CreateIceAge.asResource("block/" + path));
        return new PartialModel(CreateIceAge.asResource("block/" + path));
    }

    public static void init() {
        // init static fields
    }
}
