package com.miketies.create_ice_age.compat.jei;

import com.miketies.create_ice_age.block.IABlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;

public class SuperFreezingElement extends AnimatedKinetics {
    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        PoseStack matrixStack = guiGraphics.pose();

        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5f));
        int scale = 23;

        GuiGameElement.of(IABlocks.BASIN_FREEZER_LID.getDefaultState())
                .atLocal(0, 0, 0)
                .scale(scale)
                .render(guiGraphics);
        GuiGameElement.of(AllBlocks.BASIN.getDefaultState())
                .atLocal(0, 1, 0)
                .scale(scale)
                .render(guiGraphics);

        matrixStack.popPose();
    }
}
