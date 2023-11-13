package com.miketies.create_ice_age.blaze_freezer;

import com.jozufozu.flywheel.core.PartialModel;
import com.miketies.create_ice_age.IAPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.TransformationHelper;

public class BlazeFreezerRenderer extends SmartBlockEntityRenderer<BlazeFreezerBlockEntity> {

    private static final float PI = (float) Math.PI;
    public BlazeFreezerRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    @Override
    protected void renderSafe(BlazeFreezerBlockEntity be, float partialTicks, PoseStack ps, MultiBufferSource bufferSource, int light, int overlay) {
        super.renderSafe(be, partialTicks, ps, bufferSource, light, overlay);
        float horizontalAngle = AngleHelper.rad(be.headAngle.getValue(partialTicks));
        float animation = be.headAnimation.getValue(partialTicks) * .175f;

        ps.pushPose();

//        renderItem(be, partialTicks, animation, ps, bufferSource);
        renderBlaze(be, horizontalAngle, animation, ps, bufferSource);
//        renderBook(be, partialTicks, horizontalAngle, ps, bufferSource);

        ps.popPose();
    }

    protected void renderBlaze(BlazeFreezerBlockEntity be,
                               float horizontalAngle, float animation,
                               PoseStack ps, MultiBufferSource buffer) {
        BlockState blockState = be.getBlockState();
        BlazeFreezerBlock.FreezingLevel heatLevel = blockState.getValue(BlazeFreezerBlock.FREEZE_LEVEL);
//        boolean smouldering = heatLevel == HeatLevel.SMOULDERING;
//        boolean active = be.processingTicks > 0 && be.processingTicks < 200;
        float time = AnimationTickHolder.getRenderTime(be.getLevel());
        float renderTick = time + (be.hashCode() % 13) * 16f;
        float offsetMult = heatLevel.isAtLeast(BlazeFreezerBlock.FreezingLevel.FREEZING) ? 64 : 16;
        float offset = Mth.sin((float) ((renderTick / 16f) % (2 * Math.PI))) / offsetMult;
        float offset1 = Mth.sin((float) ((renderTick / 16f + Math.PI) % (2 * Math.PI))) / offsetMult;
        float offset2 = Mth.sin((float) ((renderTick / 16f + Math.PI / 2) % (2 * Math.PI))) / offsetMult;
        float headY = offset + (animation * .75f);
        VertexConsumer solid = buffer.getBuffer(RenderType.solid());

        ps.pushPose();
        ps.translate(0, .125, 0);

        boolean active = true;

//        PartialModel blazeModel = switch (heatLevel) {
//            case NONE -> active ? AllPartialModels.BLAZE_SUPER_ACTIVE : AllPartialModels.BLAZE_SUPER;
//            case FREEZING -> active ? AllPartialModels.BLAZE_ACTIVE : AllPartialModels.BLAZE_IDLE;
//            default -> AllPartialModels.BLAZE_INERT;
//        };

//        PartialModel blazeModel = IAPartialModels.BLAZE_FREEZER_FREEZING;
        PartialModel blazeModel = AllPartialModels.BLAZE_SUPER_ACTIVE;


        SuperByteBuffer blazeBuffer = CachedBufferer.partial(blazeModel, blockState);
        blazeBuffer.translate(0, headY, 0);
        draw(blazeBuffer, horizontalAngle, ps, solid);

        if (be.goggles) {
            PartialModel gogglesModel = blazeModel == AllPartialModels.BLAZE_INERT
                    ? AllPartialModels.BLAZE_GOGGLES_SMALL : AllPartialModels.BLAZE_GOGGLES;

            SuperByteBuffer gogglesBuffer = CachedBufferer.partial(gogglesModel, blockState);
            gogglesBuffer.translate(0, headY + 8 / 16f, 0);
            draw(gogglesBuffer, horizontalAngle, ps, solid);
        }

//        if (!smouldering) {
//            PartialModel rodsModel = heatLevel == HeatLevel.SEETHING
//                    ? AllPartialModels.BLAZE_BURNER_SUPER_RODS
//                    : AllPartialModels.BLAZE_BURNER_RODS;
//            PartialModel rodsModel2 = heatLevel == HeatLevel.SEETHING
//                    ? AllPartialModels.BLAZE_BURNER_SUPER_RODS_2
//                    : AllPartialModels.BLAZE_BURNER_RODS_2;
//            SuperByteBuffer rodsBuffer = CachedBufferer.partial(rodsModel, blockState);
//            rodsBuffer.translate(0, offset1 + animation + .125f, 0)
//                    .light(LightTexture.FULL_BRIGHT)
//                    .renderInto(ps, solid);
//
//            SuperByteBuffer rodsBuffer2 = CachedBufferer.partial(rodsModel2, blockState);
//            rodsBuffer2.translate(0, offset2 + animation - 3 / 16f, 0)
//                    .light(LightTexture.FULL_BRIGHT)
//                    .renderInto(ps, solid);
//        }

        ps.popPose();
    }



    private static void draw(SuperByteBuffer buffer, float horizontalAngle, PoseStack ms, VertexConsumer vc) {
        buffer.rotateCentered(Direction.UP, horizontalAngle)
                .light(LightTexture.FULL_BRIGHT)
                .renderInto(ms, vc);
    }
}
