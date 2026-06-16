package com.tiestoettoet.create_ice_age;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tiestoettoet.create_ice_age.content.fluids.PowderedSnowFluid;
import com.tiestoettoet.create_ice_age.content.fluids.PowderedSnowFluidType;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.createmod.catnip.theme.Color;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class AllFluids {
    private static final CreateRegistrate REGISTRATE = CreateIceAge.registrate();

    static {
        REGISTRATE.setCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB);
    }

    public static final FluidEntry<PowderedSnowFluid> POWDER_SNOW = REGISTRATE
            .virtualFluid("powder_snow", PowderedSnowFluidType::new, PowderedSnowFluid::createSource, PowderedSnowFluid::createFlowing)
            .lang("Powder Snow")
            .register();

    public static final FluidEntry<BaseFlowingFluid.Flowing> LIQUID_ICE = REGISTRATE
            .standardFluid("liquid_ice",
                    LiquidIceFluidType.create(0xA0E0FF,
                            () -> 0.10f,
                            0xFFA0E0FF)
            )
            .properties(p -> p
                    .density(917)
                    .viscosity(1000)
                    .temperature(273)
                    .supportsBoating(false)
                    .motionScale(0.0008D)
                    .canDrown(false)
                    .fallDistanceModifier(0.75f))
            .fluidProperties(p -> p
                    .levelDecreasePerBlock(1)
                    .slopeFindDistance(3))
            .renderType(() -> RenderType::translucent)
            .source(BaseFlowingFluid.Source::new)
            .block()
            .build()
            .bucket()
            .build()
            .register();

    public static void register() {}

    public static class LiquidIceFluidType extends com.simibubi.create.AllFluids.TintedFluidType {
        private Vector3f fogColor;
        private Supplier<Float> fogDistance;
        private int tintColor;

        public static FluidBuilder.FluidTypeFactory create(int fogColor, Supplier<Float> fogDistance, int tintColor) {
            return (p, s, f) -> {
                LiquidIceFluidType fluidType = new LiquidIceFluidType(p, s, f);
                fluidType.fogColor = new Color(fogColor, false).asVectorF();
                fluidType.fogDistance = fogDistance;
                fluidType.tintColor = tintColor;
                return fluidType;
            };
        }

        private LiquidIceFluidType(Properties properties, ResourceLocation stillTexture,
                                                ResourceLocation flowingTexture) {
            super(properties, stillTexture, flowingTexture);
        }

        @Override
        protected int getTintColor(FluidStack stack) {
            return tintColor;
        }

        /*
         * Removing alpha from tint prevents optifine from forcibly applying biome
         * colors to modded fluids (this workaround only works for fluids in the solid
         * render layer)
         */
        @Override
        public int getTintColor(FluidState state, BlockAndTintGetter world, BlockPos pos) {
            return tintColor;
        }

        @Override
        protected Vector3f getCustomFogColor() {
            return fogColor;
        }

        @Override
        protected float getFogDistanceModifier() {
            return fogDistance.get();
        }


    }


}
