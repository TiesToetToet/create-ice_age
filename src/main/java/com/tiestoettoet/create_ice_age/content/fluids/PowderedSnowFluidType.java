package com.tiestoettoet.create_ice_age.content.fluids;

import com.simibubi.create.AllFluids;
import com.tterrag.registrate.builders.FluidBuilder;
import net.createmod.catnip.theme.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class PowderedSnowFluidType extends AllFluids.TintedFluidType {

    private Vector3f fogColor;
    private Supplier<Float> fogDistance;
    private static final ResourceLocation STILL = ResourceLocation.fromNamespaceAndPath("minecraft", "block/powder_snow");
    private static final ResourceLocation FLOWING = ResourceLocation.fromNamespaceAndPath("minecraft", "block/powder_snow");

    public static FluidBuilder.FluidTypeFactory create(int fogColor, Supplier<Float> fogDistance) {
        return (p, s, f) -> {
            PowderedSnowFluidType type = new PowderedSnowFluidType(p, s, f);

            type.fogColor = new Color(fogColor, false).asVectorF();
            type.fogDistance = fogDistance;

            return type;
        };
    }

    public PowderedSnowFluidType(Properties properties, ResourceLocation stillTexture,
                                            ResourceLocation flowingTexture) {
        super(properties, STILL, FLOWING);
    }

    @Override
    protected int getTintColor(FluidStack stack) {
        return NO_TINT;
    }

    /*
     * Removing alpha from tint prevents optifine from forcibly applying biome
     * colors to modded fluids (this workaround only works for fluids in the solid
     * render layer)
     */
    @Override
    public int getTintColor(FluidState state, BlockAndTintGetter world, BlockPos pos) {
        return 0x00ffffff;
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
