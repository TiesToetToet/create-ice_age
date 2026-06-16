package com.tiestoettoet.create_ice_age.content.processing.super_freezer;

import com.tiestoettoet.create_ice_age.AllRecipeTypes;
import com.tiestoettoet.create_ice_age.content.processing.basin.CreateIceAgeBasinRecipe;
import com.tiestoettoet.create_ice_age.content.processing.recipe.CreateIceAgeProcessingRecipeParams;

public class FreezingMixingRecipe extends CreateIceAgeBasinRecipe {
    public FreezingMixingRecipe(CreateIceAgeProcessingRecipeParams params) {
        super(AllRecipeTypes.FREEZING_MIXING, params);
    }
}