package com.miketies.create_ice_age;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.Create;

public class IAPartialModels {
    public static final PartialModel

    BLAZE_FREEZER_FREEZING = block("blaze_freezer_freezing"); // TODO: add model
    private static PartialModel block(String path) {
        return new PartialModel(CreateIceAge.asResource("block/" + path));
    }

    public static void init() {
        // init static fields
    }
}
