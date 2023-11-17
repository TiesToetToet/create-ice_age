package com.miketies.create_ice_age.aaaaaaaaaa;

import com.miketies.create_ice_age.CreateIceAge;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import java.util.HashMap;
import java.util.Map;

public class IASpriteShifter {
    private static final Map<String, IASpriteShiftEntry> ENTRY_CACHE = new HashMap<>();

    public static IASpriteShiftEntry get(ResourceLocation originalLocation, ResourceLocation targetLocation) {
        String key = originalLocation + "->" + targetLocation;
        if (ENTRY_CACHE.containsKey(key))
            return ENTRY_CACHE.get(key);

        IASpriteShiftEntry entry = new IASpriteShiftEntry();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> entry.set(originalLocation, targetLocation));
        ENTRY_CACHE.put(key, entry);
        CreateIceAge.LOGGER.info("IASpriteShifter.get: " + entry.toString());
        return entry;
    }
}
