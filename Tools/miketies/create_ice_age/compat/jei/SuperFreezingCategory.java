package com.miketies.create_ice_age.compat.jei;

import com.miketies.create_ice_age.block.IABlocks;
import com.miketies.create_ice_age.item.IAItems;
import com.miketies.create_ice_age.super_freezer.SuperFreezingRecipe;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.compat.jei.category.ProcessingViaFanCategory;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.Pair;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.ArrayList;
import java.util.List;

public class SuperFreezingCategory extends CreateRecipeCategory<BasinRecipe> {
    private final SuperFreezingElement SuperFreezingElement = new SuperFreezingElement();
    private final AnimatedBlazeFreezer freezer = new AnimatedBlazeFreezer();
    public SuperFreezingCategory(Info<BasinRecipe> info) {
        super(info);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BasinRecipe recipe, IFocusGroup focuses) {
        List<Pair<Ingredient, MutableInt>> condensedIngredients = ItemHelper.condenseIngredients(recipe.getIngredients());

        int size = condensedIngredients.size() + recipe.getFluidIngredients().size();
        int xOffset = size < 3 ? (3 - size) * 19 / 2 : 0;
        int i = 0;

        for (Pair<Ingredient, MutableInt> pair : condensedIngredients) {
            List<ItemStack> stacks = new ArrayList<>();
            for (ItemStack itemStack : pair.getFirst().getItems()) {
                ItemStack copy = itemStack.copy();
                copy.setCount(pair.getSecond().getValue());
                stacks.add(copy);
            }

            builder
                    .addSlot(RecipeIngredientRole.INPUT, 17 + xOffset + (i % 3) * 19, 41 - (i / 3) * 19)
                    .setBackground(getRenderedSlot(), -1, -1)
                    .addItemStacks(stacks);
            i++;
        }

        for (FluidIngredient fluidIngredient : recipe.getFluidIngredients()) {
            builder
                    .addSlot(RecipeIngredientRole.INPUT, 17 + xOffset + (i % 3) * 19, 41 - (i / 3) * 19)
                    .setBackground(getRenderedSlot(), -1, -1)
                    .addIngredients(ForgeTypes.FLUID_STACK, withImprovedVisibility(fluidIngredient.getMatchingFluidStacks()))
                    .addTooltipCallback(addFluidTooltip(fluidIngredient.getRequiredAmount()));
            i++;
        }

        size = recipe.getRollableResults().size() + recipe.getFluidResults().size();
        i = 0;

        for (ProcessingOutput result : recipe.getRollableResults()) {
            int xPos = 142 - (size % 2 != 0 && i == size - 1 ? 0 : i % 2 == 0 ? 10 : -9);
            int yPos = -19 * (i / 2) + 51;

            builder
                    .addSlot(RecipeIngredientRole.OUTPUT, xPos, yPos)
                    .setBackground(getRenderedSlot(result), -1, -1)
                    .addItemStack(result.getStack())
                    .addTooltipCallback(addStochasticTooltip(result));
            i++;
        }

        for (FluidStack result : recipe.getFluidResults()) {
            int xPos = 142 - (size % 2 != 0 && i == size - 1 ? 0 : i % 2 == 0 ? 10 : -9);
            int yPos = -19 * (i / 2) + 51;

            builder
                    .addSlot(RecipeIngredientRole.OUTPUT, xPos, yPos)
                    .setBackground(getRenderedSlot(), -1, -1)
                    .addIngredient(ForgeTypes.FLUID_STACK, withImprovedVisibility(result))
                    .addTooltipCallback(addFluidTooltip(result.getAmount()));
            i++;
        }

        builder
                .addSlot(RecipeIngredientRole.RENDER_ONLY, 134, 81)
                .addItemStack(IABlocks.BLAZE_FREEZER.asStack());
        builder
                .addSlot(RecipeIngredientRole.CATALYST, 153, 81)
                .addItemStack(IAItems.ICE_CAKE.asStack());
    }


    @Override
    public void draw(BasinRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        AllGuiTextures.JEI_DOWN_ARROW.render(guiGraphics, 136, 20);

        AllGuiTextures shadow = AllGuiTextures.JEI_LIGHT;
        shadow.render(guiGraphics, 81, 88);
        SuperFreezingElement.draw(guiGraphics, 91, 47);
        freezer.draw(guiGraphics, 91, 55);

        AllGuiTextures heatbar = AllGuiTextures.JEI_HEAT_BAR;
        heatbar.render(guiGraphics, 4, 80);

        guiGraphics.drawString(
                Minecraft.getInstance().font,
                Lang.translateDirect("recipe.super_freezing.freezing"),
                9,
                86,
                0x9ED4EF,
                false
        );
    }
}
