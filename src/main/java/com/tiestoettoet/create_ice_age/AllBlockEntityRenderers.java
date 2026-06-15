package com.tiestoettoet.create_ice_age;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AllBlockEntityRenderers {
    public static BlockEntityRenderer<BlazeBurnerBlockEntity> blazeBurnerRenderer(BlockEntityRendererProvider.Context context) {
        return new BlazeBurnerRenderer(context);
    }
}
