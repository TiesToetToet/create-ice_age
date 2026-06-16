package com.tiestoettoet.create_ice_age.foundation.data.recipe;

import com.simibubi.create.api.data.recipe.FillingRecipeGen;
import com.tiestoettoet.create_ice_age.AllFluids;
import com.tiestoettoet.create_ice_age.AllItems;
import com.tiestoettoet.create_ice_age.CreateIceAge;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class CreateIceAgeFillingRecipeGen extends FillingRecipeGen {
    GeneratedRecipe

    ICE_CAKE = create("ice_cake",
        b -> b.require(AllFluids.LIQUID_ICE.get(), 250)
            .require(AllItems.ICE_CAKE_BASE.get())
            .output(AllItems.ICE_CAKE.get()));

    public CreateIceAgeFillingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreateIceAge.MOD_ID);
    }
}
