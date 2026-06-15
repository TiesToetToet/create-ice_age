package com.tiestoettoet.create_ice_age.impl.registry;

import com.tiestoettoet.create_ice_age.api.registry.CreateIceAgeDataMaps;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber
public class CreateIceAgeDataMapsImpl {
    @SubscribeEvent
    public static void registerDataMaps(RegisterDataMapTypesEvent event) {
        event.register(CreateIceAgeDataMaps.REGULAR_FREEZE_FUELS);
        event.register(CreateIceAgeDataMaps.SUPER_FREEZE_FUELS);
    }
}
