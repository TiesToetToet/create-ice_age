package com.miketies.create_ice_age;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class CreateIceAgeClient {
    public static void onCtorClient(IEventBus modEventBus) {
        modEventBus.addListener(CreateIceAgeClient::clientInit);
    }

    public static void clientInit(final FMLClientSetupEvent event) {
        IAPartialModels.init();
    }
}
