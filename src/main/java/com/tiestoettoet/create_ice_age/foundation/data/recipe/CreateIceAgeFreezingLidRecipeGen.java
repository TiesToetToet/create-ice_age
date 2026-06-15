package com.tiestoettoet.create_ice_age.foundation.data.recipe;

import com.tiestoettoet.create_ice_age.AllFluids;
import com.tiestoettoet.create_ice_age.AllItems;
import com.tiestoettoet.create_ice_age.CreateIceAge;
import com.tiestoettoet.create_ice_age.api.data.recipe.FreezingMixingRecipeGen;
import com.tiestoettoet.create_ice_age.content.processing.recipe.FreezeCondition;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

import java.util.concurrent.CompletableFuture;

public class CreateIceAgeFreezingLidRecipeGen extends FreezingMixingRecipeGen {
    GeneratedRecipe
            ICE = create("ice", b -> b.require(Blocks.COBBLESTONE)
//            .require(CreateRecipeProvider.I.zinc())
            .output(Blocks.ICE)
            .requiresFreeze(FreezeCondition.SUPER_FREEZING)),

    BLUE_ICE = create("blue_ice", b -> b.require(Blocks.PACKED_ICE)
            .require(Blocks.PACKED_ICE)
            .require(Blocks.PACKED_ICE)
            .require(Blocks.PACKED_ICE)
            .output(Blocks.BLUE_ICE)
            .requiresFreeze(FreezeCondition.FREEZING)),

    PACKED_ICE = create("packed_ice", b -> b.require(Blocks.ICE)
            .require(Blocks.ICE)
            .require(Blocks.ICE)
            .require(Blocks.ICE)
            .output(Blocks.PACKED_ICE)
            .requiresFreeze(FreezeCondition.FREEZING)),

    ICE_CAKE_BASE = create("ice_cake_base", b -> b.require(com.simibubi.create.AllItems.BLAZE_CAKE_BASE)
            .output(AllItems.ICE_CAKE_BASE.get())
            .requiresFreeze(FreezeCondition.FREEZING)),

    LIQUID_ICE = create("liquid_ice", b -> b.require(Fluids.WATER, 1000)
            .output(AllFluids.LIQUID_ICE.get(), 1000)
            .requiresFreeze(FreezeCondition.FREEZING)),

    POWDER_SNOW = create("powder_snow", b -> b.require(Fluids.WATER, 1000)
            .output(AllFluids.POWDER_SNOW.get(), 1000)
            .requiresFreeze(FreezeCondition.SUPER_FREEZING)),

    POWDER_SNOW_FROM_LIQUID_ICE = create("powder_snow_from_liquid_ice", b -> b.require(AllFluids.LIQUID_ICE.get(), 1000)
            .output(AllFluids.POWDER_SNOW.get(), 1000)
            .requiresFreeze(FreezeCondition.FREEZING))


    ;

    public CreateIceAgeFreezingLidRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreateIceAge.MOD_ID);
    }

//    public static class FreezerRecipeBuilder
//            extends StandardProcessingRecipe.Builder<FreezerLidRecipe> {
//
//        private FreezeCondition freezeCondition = FreezeCondition.NONE;
//
//        public FreezerRecipeBuilder(
//                StandardProcessingRecipe.Factory<FreezerLidRecipe> factory,
//                ResourceLocation location) {
//            super(factory, location);
//        }
//
//        public FreezerRecipeBuilder requiresFreeze(FreezeCondition condition) {
//            this.freezeCondition = condition;
//            return this;
//        }
//
//        @Override
//        public FreezerLidRecipe build() {
//            FreezerLidRecipe recipe = super.build();
//            recipe.setRequiredFreeze(freezeCondition);
//            return recipe;
//        }
//    }
}
