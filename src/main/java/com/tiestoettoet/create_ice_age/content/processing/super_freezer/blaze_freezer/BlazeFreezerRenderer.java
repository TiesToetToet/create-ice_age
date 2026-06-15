package com.tiestoettoet.create_ice_age.content.processing.super_freezer.blaze_freezer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
//import com.simibubi.create.AllPartialModels;
//import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.simibubi.create.foundation.virtualWorld.VirtualRenderWorld;
import com.tiestoettoet.create_ice_age.AllPartialModels;
import com.tiestoettoet.create_ice_age.AllSpriteShifts;
import com.tiestoettoet.create_ice_age.CreateIceAge;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.blaze_freezer.BlazeFreezerBlock.FreezingLevel;
import org.jetbrains.annotations.Nullable;

public class BlazeFreezerRenderer extends SafeBlockEntityRenderer<BlazeFreezerBlockEntity> {

    public BlazeFreezerRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(BlazeFreezerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource,
                              int light, int overlay) {
        FreezingLevel freezingLevel = be.getFreezeLevelFromBlock();
        if (freezingLevel == FreezingLevel.NONE)
            return;

        Level level = be.getLevel();
        BlockState blockState = be.getBlockState();
        float animation = be.headAnimation.getValue(partialTicks) * .175f;
        float horizontalAngle = AngleHelper.rad(be.headAngle.getValue(partialTicks));
        boolean canDrawFlame = freezingLevel.isAtLeast(FreezingLevel.FREEZING);
        boolean drawGoggles = be.goggles;
//        PartialModel drawHat = be.hat ? com.simibubi.create.AllPartialModels.TRAIN_HAT : be.stockKeeper ? com.simibubi.create.AllPartialModels.LOGISTICS_HAT : null;
        int hashCode = be.hashCode();

        renderShared(ms, null, bufferSource,
                level, blockState, freezingLevel, animation, horizontalAngle,
                canDrawFlame, drawGoggles, hashCode);
    }

    public static void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld,
                                           ContraptionMatrices matrices, MultiBufferSource bufferSource, LerpedFloat headAngle, boolean conductor) {
        BlockState state = context.state;
        FreezingLevel freezingLevel = BlazeFreezerBlock.getFreezeLevelOf(state);
        if (freezingLevel == FreezingLevel.NONE)
            return;

        if (!freezingLevel.isAtLeast(FreezingLevel.FREEZING))
            freezingLevel = FreezingLevel.FREEZING;

        Level level = context.world;
        float horizontalAngle = AngleHelper.rad(headAngle.getValue(AnimationTickHolder.getPartialTicks(level)));
        boolean drawGoggles = context.blockEntityData.contains("Goggles");
        boolean drawHat = conductor || context.blockEntityData.contains("TrainHat");
        int hashCode = context.hashCode();

        renderShared(matrices.getViewProjection(), matrices.getModel(), bufferSource,
                level, state, freezingLevel, 0, horizontalAngle,
                false, drawGoggles, hashCode);
    }

    public static void renderShared(PoseStack ms, @Nullable PoseStack modelTransform, MultiBufferSource bufferSource,
                                    Level level, BlockState blockState, FreezingLevel freezingLevel, float animation, float horizontalAngle,
                                    boolean canDrawFlame, boolean drawGoggles, int hashCode) {

        boolean blockAbove = animation > 0.125f;
        float time = AnimationTickHolder.getRenderTime(level);
        float renderTick = time + (hashCode % 13) * 16f;
        float offsetMult = freezingLevel.isAtLeast(FreezingLevel.FREEZING) ? 64 : 16;
        float offset = Mth.sin((float) ((renderTick / 16f) % (2 * Math.PI))) / offsetMult;
        float offset1 = Mth.sin((float) ((renderTick / 16f + Math.PI) % (2 * Math.PI))) / offsetMult;
        float offset2 = Mth.sin((float) ((renderTick / 16f + Math.PI / 2) % (2 * Math.PI))) / offsetMult;
        float headY = offset - (animation * .75f);

        ms.pushPose();

        var blazeModel = getBlazeModel(freezingLevel, blockAbove);

        SuperByteBuffer blazeBuffer = CachedBuffers.partial(blazeModel, blockState);
        if (modelTransform != null)
            blazeBuffer.transform(modelTransform);
        blazeBuffer.translate(0, headY, 0);
        draw(blazeBuffer, horizontalAngle, ms, bufferSource.getBuffer(RenderType.solid()));

        if (drawGoggles) {
            PartialModel gogglesModel = blazeModel == AllPartialModels.FREEZE_INERT
                    ? AllPartialModels.BLAZE_GOGGLES_SMALL : com.simibubi.create.AllPartialModels.BLAZE_GOGGLES;

            SuperByteBuffer gogglesBuffer = CachedBuffers.partial(gogglesModel, blockState);
            if (modelTransform != null)
                gogglesBuffer.transform(modelTransform);
            gogglesBuffer.translate(0, headY + 8 / 16f, 0);
            draw(gogglesBuffer, horizontalAngle, ms, bufferSource.getBuffer(RenderType.solid()));
        }

        if (freezingLevel.isAtLeast(FreezingLevel.FREEZING)) {
            PartialModel rodsModel = freezingLevel == FreezingLevel.SUPER_FREEZING ? AllPartialModels.BLAZE_FREEZER_SUPER_RODS
                    : AllPartialModels.BLAZE_FREEZER_RODS;
            PartialModel rodsModel2 = freezingLevel == FreezingLevel.SUPER_FREEZING ? AllPartialModels.BLAZE_FREEZER_SUPER_RODS_2
                    : AllPartialModels.BLAZE_FREEZER_RODS_2;

            SuperByteBuffer rodsBuffer = CachedBuffers.partial(rodsModel, blockState);
            if (modelTransform != null)
                rodsBuffer.transform(modelTransform);
            rodsBuffer.translate(0, offset1 + animation + .125f, 0)
                    .light(LightTexture.FULL_BRIGHT)
                    .renderInto(ms, bufferSource.getBuffer(RenderType.solid()));

            SuperByteBuffer rodsBuffer2 = CachedBuffers.partial(rodsModel2, blockState);
            if (modelTransform != null)
                rodsBuffer2.transform(modelTransform);
            rodsBuffer2.translate(0, offset2 + animation - 3 / 16f, 0)
                    .light(LightTexture.FULL_BRIGHT)
                    .renderInto(ms, bufferSource.getBuffer(RenderType.solid()));
        }

        if (canDrawFlame && blockAbove) {
            SpriteShiftEntry spriteShift =
                    freezingLevel == FreezingLevel.SUPER_FREEZING ? AllSpriteShifts.SUPER_FREEZER_FLAME : AllSpriteShifts.FREEZER_FLAME;

            float spriteWidth = spriteShift.getTarget()
                    .getU1()
                    - spriteShift.getTarget()
                    .getU0();

            float spriteHeight = spriteShift.getTarget()
                    .getV1()
                    - spriteShift.getTarget()
                    .getV0();

            float speed = 1 / 32f + 1 / 64f * freezingLevel.ordinal();

            double vScroll = speed * time;
            vScroll = vScroll - Math.floor(vScroll);
            vScroll = vScroll * spriteHeight / 2;

            double uScroll = speed * time / 2;
            uScroll = uScroll - Math.floor(uScroll);
            uScroll = uScroll * spriteWidth / 2;

            SuperByteBuffer flameBuffer = CachedBuffers.partial(AllPartialModels.BLAZE_FREEZER_FLAME, blockState);
            if (modelTransform != null)
                flameBuffer.transform(modelTransform);
            flameBuffer.shiftUVScrolling(spriteShift, (float) uScroll, (float) vScroll);

            VertexConsumer cutout = bufferSource.getBuffer(RenderType.cutoutMipped());
            draw(flameBuffer, horizontalAngle, ms, cutout);
        }

        ms.popPose();
    }

    public static PartialModel getBlazeModel(FreezingLevel freezingLevel, boolean blockAbove) {
        if (freezingLevel.isAtLeast(FreezingLevel.SUPER_FREEZING)) {
            return blockAbove ? AllPartialModels.FREEZE_SUPER_ACTIVE : AllPartialModels.FREEZE_SUPER;
        } else if (freezingLevel.isAtLeast(FreezingLevel.FREEZING)) {
            return blockAbove ? AllPartialModels.FREEZE_ACTIVE
                    : AllPartialModels.FREEZE_IDLE;
        } else {
            return AllPartialModels.FREEZE_INERT;
        }
    }

    private static void draw(SuperByteBuffer buffer, float horizontalAngle, PoseStack ms, VertexConsumer vc) {
        buffer.rotateCentered(horizontalAngle, Direction.UP)
                .light(LightTexture.FULL_BRIGHT)
                .renderInto(ms, vc);
    }
}
