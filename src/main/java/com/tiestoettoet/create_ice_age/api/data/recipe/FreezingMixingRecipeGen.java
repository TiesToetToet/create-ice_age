package com.tiestoettoet.create_ice_age.api.data.recipe;

import com.tiestoettoet.create_ice_age.AllRecipeTypes;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.FreezingMixingRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public abstract class FreezingMixingRecipeGen extends CreateIceAgeStandardProcessingRecipeGen<FreezingMixingRecipe> {

    public FreezingMixingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String defaultNamespace) {
        super(output, registries, defaultNamespace);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.FREEZING_MIXING;
    }
}
