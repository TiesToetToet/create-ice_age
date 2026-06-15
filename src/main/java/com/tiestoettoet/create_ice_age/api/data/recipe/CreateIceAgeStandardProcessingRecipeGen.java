package com.tiestoettoet.create_ice_age.api.data.recipe;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.tiestoettoet.create_ice_age.content.processing.recipe.CreateIceAgeProcessingRecipeParams;
import com.tiestoettoet.create_ice_age.content.processing.recipe.CreateIceAgeStandardProcessingRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public abstract class CreateIceAgeStandardProcessingRecipeGen<R extends CreateIceAgeStandardProcessingRecipe<?>> extends CreateIceAgeProcessingRecipeGen<CreateIceAgeProcessingRecipeParams, R, CreateIceAgeStandardProcessingRecipe.Builder<R>> {
    public CreateIceAgeStandardProcessingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String defaultNamespace) {
        super(output, registries, defaultNamespace);
    }

    protected CreateIceAgeStandardProcessingRecipe.Serializer<R> getSerializer() {
        return getRecipeType().getSerializer();
    }

    @Override
    protected CreateIceAgeStandardProcessingRecipe.Builder<R> getBuilder(ResourceLocation id) {
        return new CreateIceAgeStandardProcessingRecipe.Builder<>(getSerializer().factory(), id);
    }
}
