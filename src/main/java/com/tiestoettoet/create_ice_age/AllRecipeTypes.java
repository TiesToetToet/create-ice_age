package com.tiestoettoet.create_ice_age;

import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.tiestoettoet.create_ice_age.content.kinetics.fan.processing.FreezingRecipe;
import com.tiestoettoet.create_ice_age.content.processing.recipe.CreateIceAgeStandardProcessingRecipe;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.FreezingLidRecipe;
import net.createmod.catnip.lang.Lang;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public enum AllRecipeTypes implements IRecipeTypeInfo, StringRepresentable {
    FAN_FREEZING(FreezingRecipe::new),
    FREEZING_LID(FreezingLidRecipe::new);

    public final ResourceLocation id;
    private final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> serializerObject;
    @Nullable
    private final DeferredHolder<RecipeType<?>, RecipeType<?>> typeObject;
    private final Supplier<RecipeType<?>> type;

    AllRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier, Supplier<RecipeType<?>> typeSupplier, boolean registerType) {
        String name = Lang.asId(name());
        id = CreateIceAge.asResource(name);
        serializerObject = Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
        if (registerType) {
            typeObject = Registers.TYPE_REGISTER.register(name, typeSupplier);
            type = typeObject;
        } else {
            typeObject = null;
            type = typeSupplier;
        }
    }

    AllRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier) {
        String name = Lang.asId(name());
        id = CreateIceAge.asResource(name);
        CreateIceAge.LOGGER.info("Registering recipe type: {}", id);
        serializerObject = Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
        typeObject = Registers.TYPE_REGISTER.register(name, () -> RecipeType.simple(id));
        type = typeObject;
    }

    AllRecipeTypes(CreateIceAgeStandardProcessingRecipe.Factory<?> processingFactory) {
        this(() -> new CreateIceAgeStandardProcessingRecipe.Serializer<>(processingFactory));
    }

    public static void register(IEventBus eventBus) {
        Registers.SERIALIZER_REGISTER.register(eventBus);
        Registers.TYPE_REGISTER.register(eventBus);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public <T extends RecipeSerializer<?>> T getSerializer() {
        return (T) serializerObject.get();
    }

    @Override
    public <I extends RecipeInput, R extends Recipe<I>> RecipeType<R> getType() {
        return (RecipeType<R>) type.get();
    }

    public <I extends RecipeInput, R extends Recipe<I>> Optional<RecipeHolder<R>> find(I inv, Level world) {
        return world.getRecipeManager()
                .getRecipeFor(getType(), inv, world);
    }

    @Override
    public String getSerializedName() {
        return id.toString();
    }

    private static class Registers {
        private static final DeferredRegister<RecipeSerializer<?>> SERIALIZER_REGISTER = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, CreateIceAge.MOD_ID);
        private static final DeferredRegister<RecipeType<?>> TYPE_REGISTER = DeferredRegister.create(Registries.RECIPE_TYPE, CreateIceAge.MOD_ID);
    }
}
