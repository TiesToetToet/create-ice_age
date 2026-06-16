package com.tiestoettoet.create_ice_age.compat.jei.category;

import com.simibubi.create.compat.jei.category.animations.AnimatedMixer;
import com.tiestoettoet.create_ice_age.compat.jei.category.animations.AnimatedBreezeFreezer;
import com.tiestoettoet.create_ice_age.content.processing.basin.CreateIceAgeBasinRecipe;
import com.tiestoettoet.create_ice_age.content.processing.recipe.FreezeCondition;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.client.gui.GuiGraphics;

public class FreezingMixingCategory extends CreateIceAgeBasinCategory {
    private final AnimatedMixer mixer = new AnimatedMixer();
    private final AnimatedBreezeFreezer freezer = new AnimatedBreezeFreezer();

    public static FreezingMixingCategory standard(Info<CreateIceAgeBasinRecipe> info) {
        return new FreezingMixingCategory(info);
    }

    public FreezingMixingCategory(Info<CreateIceAgeBasinRecipe> info) {
        super(info, true);
    }

    @Override
    public void draw(CreateIceAgeBasinRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        super.draw(recipe, iRecipeSlotsView, graphics, mouseX, mouseY);

        FreezeCondition requiredFreeze = recipe.getRequiredFreeze();
        if (requiredFreeze != FreezeCondition.NONE)
            freezer.withFreeze(requiredFreeze.visualizeAsBreezeFreezer())
                    .draw(graphics, getBackground().getWidth() / 2 + 3, 55);
        mixer.draw(graphics, getBackground().getWidth() / 2 + 3, 34);
    }


}
