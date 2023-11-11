package com.miketies.create_ice_age.fluid;

import com.miketies.create_ice_age.CreateIceAge;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector3f;

public class IAFluidType {
    public static final ResourceLocation LIQUID_ICE_STILL_RL = new ResourceLocation(CreateIceAge.MOD_ID, "block/liquid_ice_still");
    public static final ResourceLocation LIQUID_ICE_FLOWING_RL = new ResourceLocation(CreateIceAge.MOD_ID, "block/liquid_ice_flowing");
    public static final ResourceLocation LIQUID_ICE_OVERLAY_RL = new ResourceLocation(CreateIceAge.MOD_ID, "block/liquid_ice_overlay");

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, CreateIceAge.MOD_ID);

    public static final RegistryObject<FluidType> FROZEN_ICE_FLUID_TYPE = register("liquid_ice_fluid",
            FluidType.Properties.create().density(15).viscosity(5).temperature(0));

    private static RegistryObject<FluidType> register(String name, FluidType.Properties properties) {
        return FLUID_TYPES.register(name, () -> new BaseFluidType(properties, LIQUID_ICE_STILL_RL, LIQUID_ICE_FLOWING_RL, LIQUID_ICE_OVERLAY_RL,
                0xFFb9e8ea, new Vector3f(185f / 255f, 232f / 255f, 234f / 255f)));
    }

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }
}
