package com.miketies.create_ice_age.super_freezer;

import com.miketies.create_ice_age.IARecipeTypes;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

public class SuperFreezingMixingRecipe extends SuperFreezingBasinRecipe {

    public SuperFreezingMixingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(IARecipeTypes.SUPER_FREEZING_MIXING, params);
    }

}