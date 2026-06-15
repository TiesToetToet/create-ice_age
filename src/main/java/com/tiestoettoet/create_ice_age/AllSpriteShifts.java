package com.tiestoettoet.create_ice_age;

import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SpriteShifter;

public class AllSpriteShifts {
    public static final SpriteShiftEntry FREEZER_FLAME =
            get("block/blaze_freezer_flame", "block/blaze_freezer_flame_scroll"),
            SUPER_FREEZER_FLAME = get("block/blaze_freezer_flame", "block/blaze_freezer_flame_superfreeze_scroll");

    private static SpriteShiftEntry get(String originalLocation, String targetLocation) {
        return SpriteShifter.get(CreateIceAge.asResource(originalLocation), CreateIceAge.asResource(targetLocation));
    }

    public static void register() {}
}
