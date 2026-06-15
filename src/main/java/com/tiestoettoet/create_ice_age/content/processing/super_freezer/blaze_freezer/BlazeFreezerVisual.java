package com.tiestoettoet.create_ice_age.content.processing.super_freezer.blaze_freezer;

import com.simibubi.create.content.processing.burner.ScrollInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import com.tiestoettoet.create_ice_age.AllPartialModels;
import com.tiestoettoet.create_ice_age.AllSpriteShifts;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visual.TickableVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.transform.Translate;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import dev.engine_room.flywheel.lib.visual.SimpleTickableVisual;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.blaze_freezer.BlazeFreezerBlock.FreezingLevel;

import java.util.function.Consumer;

public class BlazeFreezerVisual extends AbstractBlockEntityVisual<BlazeFreezerBlockEntity> implements SimpleDynamicVisual, SimpleTickableVisual {

    private FreezingLevel freezingLevel;

    private final TransformedInstance head;

    private final boolean isInert;

    @Nullable
    private TransformedInstance smallRods;
    @Nullable
    private TransformedInstance largeRods;
    @Nullable
    private ScrollInstance flame;
    @Nullable
    private TransformedInstance goggles;

    private boolean validBlockAbove;

    public BlazeFreezerVisual(VisualizationContext ctx, BlazeFreezerBlockEntity blockEntity, float partialTick) {
        super(ctx, blockEntity, partialTick);

        freezingLevel = FreezingLevel.COOLING;
        validBlockAbove = blockEntity.isValidBlockAbove();

        PartialModel blazeModel = BlazeFreezerRenderer.getBlazeModel(freezingLevel, validBlockAbove);
        isInert = blazeModel == AllPartialModels.FREEZE_INERT;

        head = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(blazeModel))
                .createInstance();

        head.light(LightTexture.FULL_BRIGHT);

        animate(partialTick);
    }

    @Override
    public void tick(TickableVisual.Context context) {
        blockEntity.tickAnimation();
    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {
        if (!isVisible(ctx.frustum()) || doDistanceLimitThisFrame(ctx)) {
            return;
        }

        animate(ctx.partialTick());
    }

    private void animate(float partialTicks) {
        float animation = blockEntity.headAnimation.getValue(partialTicks) * .175f;

        boolean validBlockAbove = animation > 0.125f;
        FreezingLevel freezingLevel = blockEntity.getFreezeLevelForRender();

        if (validBlockAbove != this.validBlockAbove || freezingLevel != this.freezingLevel) {
            this.validBlockAbove = validBlockAbove;

            PartialModel blazeModel = BlazeFreezerRenderer.getBlazeModel(freezingLevel, validBlockAbove);
            instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(blazeModel))
                    .stealInstance(head);

            boolean needsRods = freezingLevel.isAtLeast(FreezingLevel.FREEZING);
            boolean hasRods = this.freezingLevel.isAtLeast(FreezingLevel.FREEZING);

            if (needsRods && !hasRods) {
                PartialModel rodsModel = freezingLevel == FreezingLevel.SUPER_FREEZING ? AllPartialModels.BLAZE_FREEZER_SUPER_RODS
                        : AllPartialModels.BLAZE_FREEZER_RODS;
                PartialModel rodsModel2 = freezingLevel == FreezingLevel.SUPER_FREEZING ? AllPartialModels.BLAZE_FREEZER_SUPER_RODS_2
                        : AllPartialModels.BLAZE_FREEZER_RODS_2;

                smallRods = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(rodsModel))
                        .createInstance();
                largeRods = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(rodsModel2))
                        .createInstance();

                smallRods.light(LightTexture.FULL_BRIGHT);
                largeRods.light(LightTexture.FULL_BRIGHT);

            } else if (!needsRods && hasRods) {
                if (smallRods != null)
                    smallRods.delete();
                if (largeRods != null)
                    largeRods.delete();
                smallRods = null;
                largeRods = null;
            }

            this.freezingLevel = freezingLevel;
        }

        // Switch between showing/hiding the flame
        if (validBlockAbove && flame == null) {
            setupFlameInstance();
        } else if (!validBlockAbove && flame != null) {
            flame.delete();
            flame = null;
        }

        if (blockEntity.goggles && goggles == null) {
            goggles = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(isInert ? AllPartialModels.BLAZE_GOGGLES_SMALL : AllPartialModels.BLAZE_GOGGLES))
                    .createInstance();
            goggles.light(LightTexture.FULL_BRIGHT);
        } else if (!blockEntity.goggles && goggles != null) {
            goggles.delete();
            goggles = null;
        }

        var hashCode = blockEntity.hashCode();
        float time = AnimationTickHolder.getRenderTime(level);
        float renderTick = time + (hashCode % 13) * 16f;
        float offsetMult = freezingLevel.isAtLeast(FreezingLevel.FREEZING) ? 64 : 16;
        float offset = Mth.sin((float) ((renderTick / 16f) % (2 * Math.PI))) / offsetMult;
        float headY = offset - (animation * .75f);

        float horizontalAngle = AngleHelper.rad(blockEntity.headAngle.getValue(partialTicks));

        head.setIdentityTransform()
                .translate(getVisualPosition())
                .translateY(headY)
                .translate(Translate.CENTER)
                .rotateY(horizontalAngle)
                .translateBack(Translate.CENTER)
                .setChanged();

        if (goggles != null) {
            goggles.setIdentityTransform()
                    .translate(getVisualPosition())
                    .translateY(headY + 8 / 16f)
                    .translate(Translate.CENTER)
                    .rotateY(horizontalAngle)
                    .translateBack(Translate.CENTER)
                    .setChanged();
        }

        if (smallRods != null) {
            float offset1 = Mth.sin((float) ((renderTick / 16f + Math.PI) % (2 * Math.PI))) / offsetMult;

            smallRods.setIdentityTransform()
                    .translate(getVisualPosition())
                    .translateY(offset1 + animation + .125f)
                    .setChanged();
        }

        if (largeRods != null) {
            float offset2 = Mth.sin((float) ((renderTick / 16f + Math.PI / 2) % (2 * Math.PI))) / offsetMult;

            largeRods.setIdentityTransform()
                    .translate(getVisualPosition())
                    .translateY(offset2 + animation - 3 / 16f)
                    .setChanged();
        }
    }

    private void setupFlameInstance() {
        flame = instancerProvider().instancer(AllInstanceTypes.SCROLLING, Models.partial(AllPartialModels.BLAZE_FREEZER_FLAME))
                .createInstance();

        flame.position(getVisualPosition())
                .light(LightTexture.FULL_BRIGHT);

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

        flame.speedU = speed / 2;
        flame.speedV = speed;

        flame.scaleU = spriteWidth / 2;
        flame.scaleV = spriteHeight / 2;

        flame.diffU = spriteShift.getTarget().getU0() - spriteShift.getOriginal().getU0();
        flame.diffV = spriteShift.getTarget().getV0() - spriteShift.getOriginal().getV0();
    }

    @Override
    public void updateLight(float partialTick) {
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {

    }

    @Override
    protected void _delete() {
        head.delete();
        if (smallRods != null) {
            smallRods.delete();
        }
        if (largeRods != null) {
            largeRods.delete();
        }
        if (flame != null) {
            flame.delete();
        }
        if (goggles != null) {
            goggles.delete();
        }
    }
}
