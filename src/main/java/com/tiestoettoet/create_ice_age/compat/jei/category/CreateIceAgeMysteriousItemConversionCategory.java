//package com.tiestoettoet.create_ice_age.compat.jei.category;
//
//import com.simibubi.create.AllItems;
//import com.simibubi.create.compat.jei.ConversionRecipe;
//import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
//import com.simibubi.create.compat.jei.category.MysteriousItemConversionCategory;
//import com.simibubi.create.foundation.gui.AllGuiTextures;
//import com.tiestoettoet.create_ice_age.AllBlocks;
//import com.tiestoettoet.create_ice_age.compat.jei.CreateIceAgeConversionRecipe;
//import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
//import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
//import mezz.jei.api.recipe.IFocusGroup;
//import mezz.jei.api.recipe.RecipeIngredientRole;
//import net.minecraft.client.gui.GuiGraphics;
//import net.minecraft.world.item.crafting.RecipeHolder;
//
//import javax.annotation.ParametersAreNonnullByDefault;
//import java.util.ArrayList;
//import java.util.List;
//
//@ParametersAreNonnullByDefault
//public class CreateIceAgeMysteriousItemConversionCategory extends MysteriousItemConversionCategory {
//    public static final List<RecipeHolder<ConversionRecipe>> RECIPES = new ArrayList<>();
//
//    static {
//        RECIPES.add(CreateIceAgeConversionRecipe.create(AllItems.EMPTY_BLAZE_BURNER.asStack(), AllBlocks.BREEZE_FREEZER.asStack()));
////		RECIPES.add(ConversionRecipe.create(AllItems.CHROMATIC_COMPOUND.asStack(), AllItems.SHADOW_STEEL.asStack()));
////		RECIPES.add(ConversionRecipe.create(AllItems.CHROMATIC_COMPOUND.asStack(), AllItems.REFINED_RADIANCE.asStack()));
//    }
//
//    public CreateIceAgeMysteriousItemConversionCategory(Info<CreateIceAgeConversionRecipe> info) {
//        super(info);
//    }
//
//    @Override
//    public void setRecipe(IRecipeLayoutBuilder builder, CreateIceAgeConversionRecipe recipe, IFocusGroup focuses) {
//        builder
//                .addSlot(RecipeIngredientRole.INPUT, 27, 17)
//                .setBackground(getRenderedSlot(), -1, -1)
//                .addIngredients(recipe.getIngredients().get(0));
//        builder
//                .addSlot(RecipeIngredientRole.OUTPUT, 132, 17)
//                .setBackground(getRenderedSlot(), -1, -1)
//                .addItemStack(recipe.getRollableResults().get(0).getStack());
//    }
//
//    @Override
//    public void draw(CreateIceAgeConversionRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
//        AllGuiTextures.JEI_LONG_ARROW.render(graphics, 52, 20);
//        AllGuiTextures.JEI_QUESTION_MARK.render(graphics, 77, 5);
//    }
//}
