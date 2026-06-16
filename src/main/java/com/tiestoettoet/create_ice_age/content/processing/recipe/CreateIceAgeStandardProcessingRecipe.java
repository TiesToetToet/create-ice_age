package com.tiestoettoet.create_ice_age.content.processing.recipe;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class CreateIceAgeStandardProcessingRecipe<T extends RecipeInput> extends CreateIceAgeProcessingRecipe<T, CreateIceAgeProcessingRecipeParams> {
    public CreateIceAgeStandardProcessingRecipe(IRecipeTypeInfo typeInfo, CreateIceAgeProcessingRecipeParams params) {
        super(typeInfo, params);
    }

    @FunctionalInterface
    public interface Factory<R extends CreateIceAgeStandardProcessingRecipe<?>> extends CreateIceAgeProcessingRecipe.Factory<CreateIceAgeProcessingRecipeParams, R> {
        R create(CreateIceAgeProcessingRecipeParams params);
    }

    public static class Builder<R extends CreateIceAgeStandardProcessingRecipe<?>>
            extends CreateIceAgeProcessingRecipeBuilder<CreateIceAgeProcessingRecipeParams, R, CreateIceAgeStandardProcessingRecipe.Builder<R>> {

        public Builder(CreateIceAgeStandardProcessingRecipe.Factory<R> factory, ResourceLocation recipeId) {
            super(factory, recipeId);
        }

        @Override
        protected CreateIceAgeProcessingRecipeParams createParams() {
            return new CreateIceAgeProcessingRecipeParams();
        }

        @Override
        public CreateIceAgeStandardProcessingRecipe.Builder<R> self() {
            return this;
        }
    }

    public static class Serializer<R extends CreateIceAgeStandardProcessingRecipe<?>> implements RecipeSerializer<R> {
        private final CreateIceAgeStandardProcessingRecipe.Factory<R> factory;
        private final MapCodec<R> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, R> streamCodec;

        public Serializer(CreateIceAgeStandardProcessingRecipe.Factory<R> factory) {
            this.factory = factory;
            this.codec = CreateIceAgeProcessingRecipe.codec(factory, CreateIceAgeProcessingRecipeParams.CODEC);
            this.streamCodec = CreateIceAgeProcessingRecipe.streamCodec(factory, CreateIceAgeProcessingRecipeParams.STREAM_CODEC);
        }

        @Override
        public MapCodec<R> codec() {
            return codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, R> streamCodec() {
            return streamCodec;
        }

        public CreateIceAgeStandardProcessingRecipe.Factory<R> factory() {
            return factory;
        }
    }
}
