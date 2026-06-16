package com.tiestoettoet.create_ice_age;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.simibubi.create.infrastructure.data.CreateDatagen;
import com.tiestoettoet.create_ice_age.content.fluids.PowderedSnowBucketHandler;
import com.tiestoettoet.create_ice_age.infrastructure.data.CreateIceAgeDataGen;
import com.tiestoettoet.create_ice_age.item.ModItems;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;

@Mod(CreateIceAge.MOD_ID)
public class CreateIceAge {

    public static final String MOD_ID = "create_ice_age";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static CreateRegistrate registrate;

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

    static {
        REGISTRATE.defaultCreativeTab((ResourceKey<CreativeModeTab>) null)
                .setTooltipModifierFactory(
                        item -> new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                                .andThen(TooltipModifier.mapNull(KineticStats.create(item))));
    }

    public CreateIceAge(IEventBus modEventBus, ModContainer modContainer) {onCtor(modEventBus, modContainer); }

    public static void onCtor(IEventBus modEventBus, ModContainer modContainer) {

        ModLoadingContext modLoadingContext = ModLoadingContext.get();

        REGISTRATE.registerEventListeners(modEventBus);

        AllCreativeModeTabs.register(modEventBus);
        AllBlockEntityTypes.register();
        AllRecipeTypes.register(modEventBus);
        AllParticleTypes.register(modEventBus);
        AllBlocks.register();
        AllItems.register();
//        AllFluids.FLUID_TYPES.register(modEventBus);
//        AllFluids.FLUIDS.register(modEventBus);
        AllFluids.register();
        AllSpriteShifts.register();


        modEventBus.addListener(CreateIceAge::onRegister);
        modEventBus.addListener(EventPriority.HIGHEST, CreateIceAgeDataGen::gatherDataHighPriority);
        modEventBus.addListener(EventPriority.HIGHEST, CreateIceAgeDataGen::gatherData);
        modEventBus.addListener(PowderedSnowBucketHandler::register);

        ModItems.register(modEventBus);
    }

    public static void onRegister( final RegisterEvent event ) {
    }

    public static CreateRegistrate registrate() { return REGISTRATE; }


    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("create_ice_age ServerStartingEvent: {}", event);
    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("create_ice_age FMLClientSetupEvent: {}", event);
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }


}
