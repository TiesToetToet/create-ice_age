package com.tiestoettoet.create_ice_age.compat.jei.category.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.tiestoettoet.create_ice_age.AllBlocks;
import com.tiestoettoet.create_ice_age.AllPartialModels;
import com.tiestoettoet.create_ice_age.AllSpriteShifts;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.blaze_freezer.BlazeFreezerBlock;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;

public class AnimatedBlazeFreezer extends AnimatedKinetics {
    private BlazeFreezerBlock.FreezingLevel freezingLevel;

    public AnimatedBlazeFreezer withFreeze(BlazeFreezerBlock.FreezingLevel freezingLevel) {
        this.freezingLevel = freezingLevel;
        return this;
    }

    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5f));
        int scale = 23;

        float offset = (Mth.sin(AnimationTickHolder.getRenderTime() / 16f) + 0.5f) / 16f;

        blockElement(AllBlocks.BLAZE_FREEZER.getDefaultState()).atLocal(0, 1.65, 0)
                .scale(scale)
                .render(graphics);

        PartialModel blaze =
                freezingLevel == BlazeFreezerBlock.FreezingLevel.SUPER_FREEZING ? AllPartialModels.FREEZE_SUPER : AllPartialModels.FREEZE_ACTIVE;
        PartialModel rods2 = freezingLevel == BlazeFreezerBlock.FreezingLevel.SUPER_FREEZING ? AllPartialModels.BLAZE_FREEZER_SUPER_RODS_2
                : AllPartialModels.BLAZE_FREEZER_RODS_2;

        blockElement(blaze).atLocal(1, 1.8, 1)
                .rotate(0, 180, 0)
                .scale(scale)
                .render(graphics);
        blockElement(rods2).atLocal(1, 1.7 + offset, 1)
                .rotate(0, 180, 0)
                .scale(scale)
                .render(graphics);

        matrixStack.scale(scale, -scale, scale);
        matrixStack.translate(0, -1.8, 0);

        SpriteShiftEntry spriteShift =
                freezingLevel == BlazeFreezerBlock.FreezingLevel.SUPER_FREEZING ? AllSpriteShifts.SUPER_FREEZER_FLAME : AllSpriteShifts.FREEZER_FLAME;

        float spriteWidth = spriteShift.getTarget()
                .getU1()
                - spriteShift.getTarget()
                .getU0();

        float spriteHeight = spriteShift.getTarget()
                .getV1()
                - spriteShift.getTarget()
                .getV0();

        float time = AnimationTickHolder.getRenderTime(Minecraft.getInstance().level);
        float speed = 1 / 32f + 1 / 64f * freezingLevel.ordinal();

        double vScroll = speed * time;
        vScroll = vScroll - Math.floor(vScroll);
        vScroll = vScroll * spriteHeight / 2;

        double uScroll = speed * time / 2;
        uScroll = uScroll - Math.floor(uScroll);
        uScroll = uScroll * spriteWidth / 2;

        CachedBuffers.partial(AllPartialModels.BLAZE_FREEZER_FLAME, Blocks.AIR.defaultBlockState())
                .shiftUVScrolling(spriteShift, (float) uScroll, (float) vScroll)
                .light(LightTexture.FULL_BRIGHT)
                .renderInto(matrixStack, graphics.bufferSource().getBuffer(RenderType.cutoutMipped()));
        matrixStack.popPose();
    }
}
