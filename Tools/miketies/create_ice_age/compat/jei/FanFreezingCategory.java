package com.miketies.create_ice_age.compat.jei;

import com.miketies.create_ice_age.block.IABlocks;
import com.miketies.create_ice_age.fan.FanFreezingRecipe;
import com.simibubi.create.compat.jei.category.ProcessingViaFanCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;

public class FanFreezingCategory extends ProcessingViaFanCategory.MultiOutput<FanFreezingRecipe>{
    public FanFreezingCategory(Info<FanFreezingRecipe> info) {
        super(info);
    }

    @Override
    protected void renderAttachedBlock(GuiGraphics graphics) {
        GuiGameElement.of(IABlocks.LIQUID_ICE_BLOCK.get().defaultBlockState())
            .scale(SCALE)
            .atLocal(0, 0, 2)
            .lighting(AnimatedKinetics.DEFAULT_LIGHTING)
            .render(graphics);
    }
}
