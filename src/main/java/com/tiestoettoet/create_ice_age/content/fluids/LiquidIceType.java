package com.tiestoettoet.create_ice_age.content.fluids;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.simibubi.create.AllFluids;
import com.tiestoettoet.create_ice_age.CreateIceAge;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.function.Consumer;

public class LiquidIceType extends AllFluids.TintedFluidType {

    public LiquidIceType(Properties properties,
                         ResourceLocation stillTexture,
                         ResourceLocation flowingTexture) {
        super(properties, stillTexture, flowingTexture);
    }

    @Override
    protected int getTintColor(FluidStack stack) {
        return NO_TINT;
    }

    @Override
    protected int getTintColor(FluidState state,
                               BlockAndTintGetter getter,
                               BlockPos pos) {
        return 0x00FFFFFF;
    }
}
