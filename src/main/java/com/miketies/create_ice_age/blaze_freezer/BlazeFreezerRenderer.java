package com.miketies.create_ice_age.blaze_freezer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class BlazeFreezerRenderer extends SafeBlockEntityRenderer<BlazeFreezerBlockEntity> {
    public BlazeFreezerRenderer(BlockEntityRendererProvider.Context context) {
//        super(context);
    }
    @Override
    protected void renderSafe(BlazeFreezerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

    }
}
