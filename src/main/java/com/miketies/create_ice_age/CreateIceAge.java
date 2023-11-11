package com.miketies.create_ice_age;

import com.miketies.create_ice_age.block.IABlocks;
import com.miketies.create_ice_age.fan.IAFanProcessingTypes;
import com.miketies.create_ice_age.fluid.IAFluidType;
import com.miketies.create_ice_age.fluid.IAFluids;
import com.miketies.create_ice_age.item.IAItems;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CreateIceAge.MOD_ID)
public class CreateIceAge {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "create_ice_age";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate ICE_AGE_REGISTRATE = CreateRegistrate.create(MOD_ID)
            .setCreativeTab(IACreativeTab.ICE_AGE_CREATIVE_TAB);

    static {
        ICE_AGE_REGISTRATE.setTooltipModifierFactory(item -> {
            return new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)
                    .andThen(TooltipModifier.mapNull(KineticStats.create(item)));
        });
    }

    public CreateIceAge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        IABlocks.register(modEventBus);

        IAItems.register(modEventBus);

        IACreativeTab.register(modEventBus);

        IAFluids.register(modEventBus);
        IAFluidType.register(modEventBus);


        IARecipeTypes.register(modEventBus);
        IAFanProcessingTypes.register();


        ICE_AGE_REGISTRATE.registerEventListeners(modEventBus);


        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
//        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
//            ItemBlockRenderTypes.setRenderLayer(IAFluids.SOURCE_LIQUID_ICE.get(), RenderType.translucent());
//            ItemBlockRenderTypes.setRenderLayer(IAFluids.FLOWING_LIQUID_ICE.get(), RenderType.translucent());
        }
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
