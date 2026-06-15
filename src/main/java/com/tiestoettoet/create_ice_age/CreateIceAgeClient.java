package com.tiestoettoet.create_ice_age;

import com.simibubi.create.compat.Mods;
import com.simibubi.create.compat.ftb.FTBIntegration;
import com.simibubi.create.compat.sodium.SodiumCompat;
import com.simibubi.create.foundation.render.AllInstanceTypes;
//import com.tiestoettoet.create_ice_age.client.AllFluidClientExtensions;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBufferCache;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = CreateIceAge.MOD_ID, dist = Dist.CLIENT)
public class CreateIceAgeClient {
    public CreateIceAgeClient(IEventBus modEventBus) {
        onCtorClient(modEventBus);
    }

    public static void onCtorClient(IEventBus modEventBus) {
        IEventBus neoEventBus = NeoForge.EVENT_BUS;

        modEventBus.addListener(CreateIceAgeClient::clientInit);
        modEventBus.addListener(AllParticleTypes::registerFactories);
//        modEventBus.addListener(AllFluidClientExtensions::register);

        AllInstanceTypes.init();

        Mods.FTBLIBRARY.executeIfInstalled(() -> () -> FTBIntegration.init(modEventBus, neoEventBus));
        Mods.SODIUM.executeIfInstalled(() -> () -> SodiumCompat.init(modEventBus, neoEventBus));
    }

    public static void clientInit(final FMLClientSetupEvent event) {
        SuperByteBufferCache.getInstance().registerCompartment(CachedBuffers.PARTIAL);

        System.out.println("Create Ice Age client setup complete.");
        AllPartialModels.init();
//        AllSpriteShifts.init();

    }
}
