package com.tiestoettoet.create_ice_age;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerRenderer;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.breeze_freezer.BreezeFreezerBlockEntity;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.breeze_freezer.BreezeFreezerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AllBlockEntityRenderers {
    public static BlockEntityRenderer<BreezeFreezerBlockEntity> breezeFreezerRenderer(BlockEntityRendererProvider.Context context) {
        return new BreezeFreezerRenderer(context);
    }
}
