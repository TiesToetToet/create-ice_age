package com.miketies.create_ice_age.aaaaaaaaaa;

import com.miketies.create_ice_age.CreateIceAge;
import com.simibubi.create.AllSpriteShifts;

public class IASpriteShifts extends AllSpriteShifts {
    public static final IASpriteShiftEntry FREEZER_FLAME =
        get("block/blaze_freezer_flame", "block/blaze_freezer_flame_scroll");

    private static IASpriteShiftEntry get(String originalLocation, String targetLocation) {
        CreateIceAge.LOGGER.info("IASpriteShifts.get: originalLocation = " + originalLocation + ", targetLocation = " + targetLocation);
        CreateIceAge.LOGGER.info("IASpriteShifts.get: CreateIceAge.asResource(originalLocation) = " + CreateIceAge.asResource(originalLocation) + ", CreateIceAge.asResource(targetLocation) = " + CreateIceAge.asResource(targetLocation));
        return IASpriteShifter.get(CreateIceAge.asResource(originalLocation), CreateIceAge.asResource(targetLocation));
    }

    static {
        populateMaps();
    }

    private static void populateMaps() {

    }
}
