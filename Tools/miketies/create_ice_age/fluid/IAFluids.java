package com.miketies.create_ice_age.fluid;

import com.miketies.create_ice_age.CreateIceAge;
import com.miketies.create_ice_age.block.IABlocks;
import com.miketies.create_ice_age.item.IAItems;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class IAFluids {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, CreateIceAge.MOD_ID);


    public static final RegistryObject<FlowingFluid> SOURCE_LIQUID_ICE = FLUIDS.register("liquid_ice_fluid",
            () -> new ForgeFlowingFluid.Source(IAFluids.LIQUID_ICE_PROPERTIES));

    public static final RegistryObject<FlowingFluid> FLOWING_LIQUID_ICE = FLUIDS.register("flowing_liquid_ice",
            () -> new ForgeFlowingFluid.Flowing(IAFluids.LIQUID_ICE_PROPERTIES));

    public static final ForgeFlowingFluid.Properties LIQUID_ICE_PROPERTIES = new ForgeFlowingFluid.Properties(
            IAFluidType.LIQUID_ICE_FLUID_TYPE, SOURCE_LIQUID_ICE, FLOWING_LIQUID_ICE)
            .slopeFindDistance(1)
            .levelDecreasePerBlock(3)
            .block(IABlocks.LIQUID_ICE_BLOCK)
            .bucket(IAItems.LIQUID_ICE_BUCKET);


    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
