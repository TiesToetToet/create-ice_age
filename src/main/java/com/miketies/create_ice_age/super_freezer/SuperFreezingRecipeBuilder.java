package com.miketies.create_ice_age.super_freezer;

import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

public class SuperFreezingRecipeBuilder extends ProcessingRecipeBuilder {

    public SuperFreezingRecipeBuilder(ProcessingRecipeFactory factory, ResourceLocation recipeId) {
        super((ProcessingRecipeBuilder.ProcessingRecipeFactory) factory, recipeId);
    }

    public interface ProcessingRecipeFactory<T extends ProcessingRecipe<?>> {
        T create(SuperFreezingRecipeBuilder.ProcessingRecipeParams params);
    }

    public static class ProcessingRecipeParams {

        public ResourceLocation id;
        public NonNullList<Ingredient> ingredients;
        public NonNullList<ProcessingOutput> results;
        public NonNullList<FluidIngredient> fluidIngredients;
        public NonNullList<FluidStack> fluidResults;
        public int processingDuration;
        public HeatCondition requiredHeat;

        public boolean keepHeldItem;

        protected ProcessingRecipeParams(ResourceLocation id) {
            this.id = id;
            ingredients = NonNullList.create();
            results = NonNullList.create();
            fluidIngredients = NonNullList.create();
            fluidResults = NonNullList.create();
            processingDuration = 0;
            requiredHeat = HeatCondition.NONE;
            keepHeldItem = false;
        }

    }
}
