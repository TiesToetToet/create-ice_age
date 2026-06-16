package com.tiestoettoet.create_ice_age.foundation.data.recipe;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.tiestoettoet.create_ice_age.api.data.recipe.CreateIceAgeProcessingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class CreateIceAgeRecipeProvider extends RecipeProvider {
    static final List<CreateIceAgeProcessingRecipeGen<?, ?, ?>> CUSTOM_GENERATORS = new ArrayList<>();
    static final List<ProcessingRecipeGen<?, ?, ?>> GENERATORS = new ArrayList<>();
    static final int BUCKET = FluidType.BUCKET_VOLUME;
    static final int BOTTLE = 250;

    public CreateIceAgeRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        buildMachineRecipes(recipeOutput);
    }

    private void buildMachineRecipes(RecipeOutput output) {

    }

    public static void registerAllProcessing(DataGenerator gen, PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        CUSTOM_GENERATORS.add(new CreateIceAgeFreezingMixingRecipeGen(output, registries));
        GENERATORS.add(new CreateIceAgeFillingRecipeGen(output, registries));

        gen.addProvider(true, new DataProvider() {

            @Override
            public String getName() {
                return "Create Ice Age's Processing Recipes";
            }

            @Override
            public CompletableFuture<?> run(CachedOutput dc) {
                return CompletableFuture.allOf(
                        Stream.concat(
                                        GENERATORS.stream().map(gen -> (DataProvider) gen),
                                        CUSTOM_GENERATORS.stream().map(gen -> (DataProvider) gen)
                                )
                                .map(gen -> gen.run(dc))
                                .toArray(CompletableFuture[]::new)
                );
            }
        });
    }

    protected static class I {

    }

}
