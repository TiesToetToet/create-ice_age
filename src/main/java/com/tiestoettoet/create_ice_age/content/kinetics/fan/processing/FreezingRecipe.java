package com.tiestoettoet.create_ice_age.content.kinetics.fan.processing;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.tiestoettoet.create_ice_age.AllRecipeTypes;
import com.tiestoettoet.create_ice_age.content.processing.recipe.CreateIceAgeProcessingRecipeParams;
import com.tiestoettoet.create_ice_age.content.processing.recipe.CreateIceAgeStandardProcessingRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public class FreezingRecipe extends CreateIceAgeStandardProcessingRecipe<SingleRecipeInput> {

    public FreezingRecipe(CreateIceAgeProcessingRecipeParams params) {
        super(AllRecipeTypes.FAN_FREEZING, params);
    }

    @Override
    public boolean matches(SingleRecipeInput inv, Level worldIn) {
        if (inv.isEmpty())
            return false;
        return ingredients.get(0).test(inv.getItem(0));
    }

    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 12;
    }
}
