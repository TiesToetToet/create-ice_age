package com.miketies.create_ice_age.compat.jei;

import com.jozufozu.flywheel.core.PartialModel;
import com.miketies.create_ice_age.IAPartialModels;
import com.miketies.create_ice_age.block.IABlocks;
import com.miketies.create_ice_age.super_freezer.blaze_freezer.BlazeFreezerBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.render.CachedBufferer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.world.level.block.Blocks;

public class AnimatedBlazeFreezer extends AnimatedKinetics {
    private BlazeFreezerBlock.FreezingLevel freezingLevel;

    public AnimatedBlazeFreezer withFreeze(BlazeFreezerBlock.FreezingLevel freezingLevel) {
        this.freezingLevel = freezingLevel;
        return this;
    }
    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        PoseStack matrixStack = guiGraphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5f));
        int scale = 23;

        float offset = (Mth.sin(AnimationTickHolder.getRenderTime() / 16f) + 0.5f) / 16f;

        blockElement(IABlocks.BLAZE_FREEZER.getDefaultState())
                .atLocal(0, 1.65, 0)
                .scale(scale)
                .render(guiGraphics);

        PartialModel blaze = IAPartialModels.BLAZE_FREEZER_IDLE_HEAD;
        PartialModel rod2 = IAPartialModels.BLAZE_FREEZER_RODS_BIG;

        blockElement(blaze)
                .atLocal(1, 1.8, 1)
                .rotate(0, 180, 0)
                .scale(scale)
                .render(guiGraphics);

        blockElement(rod2)
                .atLocal(1, 1.7 + offset, 1)
                .rotate(0, 180, 0)
                .scale(scale)
                .render(guiGraphics);

        matrixStack.scale(scale, -scale, scale);
        matrixStack.translate(0, -1.8, 0);

        SpriteShiftEntry spriteShift =AllSpriteShifts.SUPER_BURNER_FLAME;

        float spriteWidth = spriteShift.getTarget()
                .getU1()
                - spriteShift.getTarget()
                .getU0();

        float spriteHeight = spriteShift.getTarget()
                .getV1()
                - spriteShift.getTarget()
                .getV0();

        float time = AnimationTickHolder.getRenderTime(Minecraft.getInstance().level);
        float speed = 1 / 32f + 1 / 64f * 3;

        double vScroll = speed * time;
        vScroll = vScroll - Math.floor(vScroll);
        vScroll = vScroll * spriteHeight / 2;

        double uScroll = speed * time / 2;
        uScroll = uScroll - Math.floor(uScroll);
        uScroll = uScroll * spriteWidth / 2;

        Minecraft mc = Minecraft.getInstance();
        MultiBufferSource.BufferSource buffer = mc.renderBuffers()
                .bufferSource();
        VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());
        CachedBufferer.partial(AllPartialModels.BLAZE_BURNER_FLAME, Blocks.AIR.defaultBlockState())
                .shiftUVScrolling(spriteShift, (float) uScroll, (float) vScroll)
                .light(LightTexture.FULL_BRIGHT)
                .renderInto(matrixStack, vb);

        matrixStack.popPose();
    }
}
