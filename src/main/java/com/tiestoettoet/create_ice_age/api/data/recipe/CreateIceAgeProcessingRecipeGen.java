package com.tiestoettoet.create_ice_age.api.data.recipe;

import com.simibubi.create.api.data.recipe.BaseRecipeProvider;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.tiestoettoet.create_ice_age.CreateIceAge;
import com.tiestoettoet.create_ice_age.content.processing.recipe.CreateIceAgeProcessingRecipe;
import com.tiestoettoet.create_ice_age.content.processing.recipe.CreateIceAgeProcessingRecipeBuilder;
import com.tiestoettoet.create_ice_age.content.processing.recipe.CreateIceAgeProcessingRecipeParams;
import net.createmod.catnip.registry.RegisteredObjectsHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public abstract class CreateIceAgeProcessingRecipeGen<P extends CreateIceAgeProcessingRecipeParams, R extends CreateIceAgeProcessingRecipe<?, P>, B extends CreateIceAgeProcessingRecipeBuilder<P, R, B>> extends BaseRecipeProvider {

    public CreateIceAgeProcessingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String defaultNamespace) {
        super(output, registries, defaultNamespace);
    }

    /**
     * Create a processing recipe with a single itemstack ingredient, using its id
     * as the name of the recipe
     */
    protected GeneratedRecipe create(String namespace, Supplier<ItemLike> singleIngredient, UnaryOperator<B> transform) {
        GeneratedRecipe generatedRecipe = c -> {
            ItemLike itemLike = singleIngredient.get();
            transform
                    .apply(getBuilder(ResourceLocation.fromNamespaceAndPath(namespace, RegisteredObjectsHelper.getKeyOrThrow(itemLike.asItem()).getPath())).withItemIngredients(Ingredient.of(itemLike)))
                    .build(c);
        };
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    /**
     * Create a processing recipe with a single itemstack ingredient, using its id
     * as the name of the recipe
     */
    protected GeneratedRecipe create(Supplier<ItemLike> singleIngredient, UnaryOperator<B> transform) {
        return create(CreateIceAge.MOD_ID, singleIngredient, transform);
    }

    protected GeneratedRecipe createWithDeferredId(Supplier<ResourceLocation> name, UnaryOperator<B> transform) {
        GeneratedRecipe generatedRecipe =
                c -> transform.apply(getBuilder(name.get()))
                        .build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    /**
     * Create a new processing recipe, with recipe definitions provided by the
     * function
     */
    protected GeneratedRecipe create(ResourceLocation name, UnaryOperator<B> transform) {
        return createWithDeferredId(() -> name, transform);
    }

    /**
     * Create a new processing recipe, with recipe definitions provided by the
     * function, under the default namespace
     */
    protected GeneratedRecipe create(String name, UnaryOperator<B> transform) {
        return create(asResource(name), transform);
    }

    protected abstract IRecipeTypeInfo getRecipeType();

    protected abstract B getBuilder(ResourceLocation id);

    protected Supplier<ResourceLocation> idWithSuffix(Supplier<ItemLike> item, String suffix) {
        return () -> {
            ResourceLocation registryName = RegisteredObjectsHelper.getKeyOrThrow(item.get()
                    .asItem());
            return asResource(registryName.getPath() + suffix);
        };
    }

    /**
     * Gets a display name for this recipe generator.
     * It is recommended to override this for a prettier name, however that is not
     * required.
     */
//    @NotNull
//    @Override
//    public String getName() {
//        return modid + "'s processing recipes: " + getRecipeType().getId()
//                .getPath();
//    }

}
