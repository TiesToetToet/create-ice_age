package com.miketies.create_ice_age.super_freezer;

import com.miketies.create_ice_age.IARecipeTypes;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

public class SuperFreezingRecipe extends BasinRecipe {
    public SuperFreezingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(IARecipeTypes.SUPER_FREEZING, params);
    }

    @Override
    public String toString() {
        return "SuperFreezingRecipe{" +
                "id=" + id +
                ", ingredients=" + ingredients +
                ", results=" + results +
                ", processingDuration=" + processingDuration +
                ", requiredHeat=" + requiredHeat +
                ", fluidResults=" + fluidResults +
                '}';
    }
}
