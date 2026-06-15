package com.tiestoettoet.create_ice_age.content.processing.super_freezer;

import com.tiestoettoet.create_ice_age.AllRecipeTypes;
import com.tiestoettoet.create_ice_age.content.processing.basin.CreateIceAgeBasinRecipe;
import com.tiestoettoet.create_ice_age.content.processing.recipe.CreateIceAgeProcessingRecipeParams;

public class FreezingLidRecipe extends CreateIceAgeBasinRecipe {
    public FreezingLidRecipe(CreateIceAgeProcessingRecipeParams params) {
        super(AllRecipeTypes.FREEZING_LID, params);
    }
}