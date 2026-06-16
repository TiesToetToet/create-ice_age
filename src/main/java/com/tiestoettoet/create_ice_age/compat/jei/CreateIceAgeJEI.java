package com.tiestoettoet.create_ice_age.compat.jei;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllFluids;
import com.simibubi.create.compat.jei.*;
import com.simibubi.create.compat.jei.category.*;
import com.simibubi.create.compat.jei.ToolboxColoringRecipeMaker;
import com.simibubi.create.content.equipment.blueprint.BlueprintScreen;
import com.simibubi.create.content.fluids.potion.PotionFluid;
import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelSetItemScreen;
import com.simibubi.create.content.logistics.filter.AbstractFilterScreen;
import com.simibubi.create.content.logistics.redstoneRequester.RedstoneRequesterScreen;
import com.simibubi.create.content.logistics.stockTicker.StockKeeperRequestScreen;
import com.simibubi.create.content.redstone.link.controller.LinkedControllerScreen;
import com.simibubi.create.content.trains.schedule.ScheduleScreen;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.item.ItemHelper;
import com.tiestoettoet.create_ice_age.CreateIceAge;
//import com.tiestoettoet.create_ice_age.compat.jei.category.CreateIceAgeMysteriousItemConversionCategory;
import com.tiestoettoet.create_ice_age.compat.jei.category.CreateIceAgeRecipeCategory;
import com.tiestoettoet.create_ice_age.compat.jei.category.FreezingMixingCategory;
import com.tiestoettoet.create_ice_age.content.processing.basin.CreateIceAgeBasinRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

@JeiPlugin
@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class CreateIceAgeJEI implements IModPlugin {
    private static final ResourceLocation ID = CreateIceAge.asResource("jei_plugin");

    private final List<CreateIceAgeRecipeCategory<?>> allCategories = new ArrayList<>();

    private IIngredientManager ingredientManager;

    public static IJeiRuntime runtime;

    private void loadCategories() {
        allCategories.clear();

        CreateIceAgeRecipeCategory<?>

                freezing_mixing = builder(CreateIceAgeBasinRecipe.class)
                        .addTypedRecipes(com.tiestoettoet.create_ice_age.AllRecipeTypes.FREEZING_MIXING)
                        .catalyst(AllBlocks.MECHANICAL_MIXER::get)
                        .catalyst(AllBlocks.BASIN::get)
                        .doubleItemIcon(AllBlocks.MECHANICAL_MIXER.get(), AllBlocks.BASIN.get())
                        .emptyBackground(177, 103)
                        .build("freezing_mixing", FreezingMixingCategory::standard);




    }

    private <T extends Recipe<? extends RecipeInput>> CreateIceAgeJEI.CategoryBuilder<T> builder(Class<T> recipeClass) {
        return new CreateIceAgeJEI.CategoryBuilder<>(recipeClass);
    }

    @Override
    @NotNull
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        loadCategories();
        registration.addRecipeCategories(allCategories.toArray(IRecipeCategory[]::new));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ingredientManager = registration.getIngredientManager();

        MysteriousItemConversionCategory.RECIPES.add(
                ConversionRecipe.create(
                        com.simibubi.create.AllItems.EMPTY_BLAZE_BURNER.asStack(),
                        com.tiestoettoet.create_ice_age.AllBlocks.BREEZE_FREEZER.asStack()
                )
        );

        allCategories.forEach(c -> c.registerRecipes(registration));

        registration.addRecipes(RecipeTypes.CRAFTING,
                ToolboxColoringRecipeMaker.createRecipes().toList());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        allCategories.forEach(c -> c.registerCatalysts(registration));
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(new BlueprintTransferHandler(), RecipeTypes.CRAFTING);
        registration.addUniversalRecipeTransferHandler(new StockKeeperTransferHandler(registration.getJeiHelpers()));
    }

    @Override
    public <T> void registerFluidSubtypes(ISubtypeRegistration registration, IPlatformFluidHelper<T> platformFluidHelper) {
        PotionFluidSubtypeInterpreter interpreter = new PotionFluidSubtypeInterpreter();
        PotionFluid potionFluid = AllFluids.POTION.get();
        registration.registerSubtypeInterpreter(NeoForgeTypes.FLUID_STACK, potionFluid.getSource(), interpreter);
        registration.registerSubtypeInterpreter(NeoForgeTypes.FLUID_STACK, potionFluid.getFlowing(), interpreter);
    }

    @Override
    public void registerExtraIngredients(IExtraIngredientRegistration registration) {
        RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess();
        List<Holder.Reference<Potion>> potions = registryAccess.lookupOrThrow(Registries.POTION)
                .listElements()
                .toList();
        Collection<FluidStack> potionFluids = new ArrayList<>(potions.size() * 3);
        Set<Set<Holder<MobEffect>>> visitedEffects = new HashSet<>();
        for (Holder.Reference<Potion> potion : potions) {
            // @goshante: Ingame potion fluids always have Bottle tag that specifies
            // to what bottle type this potion belongs
            // Potion fluid without this tag wouldn't be recognized by other mods

//			for (PotionFluid.BottleType bottleType : PotionFluid.BottleType.values()) {
//				FluidStack potionFluid = PotionFluid.of(1000, new PotionContents(potion), bottleType);
//				potionFluids.add(potionFluid);
//			}

            PotionContents potionContents = new PotionContents(potion);

            if (potionContents.hasEffects()) {
                Set<Holder<MobEffect>> effectSet = new HashSet<>();
                potionContents.forEachEffect(mei -> effectSet.add(mei.getEffect()));
                if (!visitedEffects.add(effectSet))
                    continue;
            }

            potionFluids.add(PotionFluid.of(1000, potionContents, PotionFluid.BottleType.REGULAR));
        }
        registration.addExtraIngredients(NeoForgeTypes.FLUID_STACK, potionFluids);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGenericGuiContainerHandler(AbstractSimiContainerScreen.class, new SlotMover());

        registration.addGhostIngredientHandler(AbstractFilterScreen.class, new GhostIngredientHandler());
        registration.addGhostIngredientHandler(BlueprintScreen.class, new GhostIngredientHandler());
        registration.addGhostIngredientHandler(LinkedControllerScreen.class, new GhostIngredientHandler());
        registration.addGhostIngredientHandler(ScheduleScreen.class, new GhostIngredientHandler());
        registration.addGhostIngredientHandler(RedstoneRequesterScreen.class, new GhostIngredientHandler());
        registration.addGhostIngredientHandler(FactoryPanelSetItemScreen.class, new GhostIngredientHandler());

        registration.addGuiContainerHandler(StockKeeperRequestScreen.class, new StockKeeperGuiContainerHandler(ingredientManager));
    }

    private class CategoryBuilder<T extends Recipe<?>> extends CreateIceAgeRecipeCategory.Builder<T> {
        public CategoryBuilder(Class<? extends T> recipeClass) {
            super(recipeClass);
        }

        @Override
        public CreateIceAgeRecipeCategory<T> build(ResourceLocation id, CreateIceAgeRecipeCategory.Factory<T> factory) {
            CreateIceAgeRecipeCategory<T> category = super.build(id, factory);
            allCategories.add(category);
            return category;
        }
    }

    public static void consumeAllRecipes(Consumer<? super RecipeHolder<?>> consumer) {
        Minecraft.getInstance()
                .getConnection()
                .getRecipeManager()
                .getRecipes()
                .forEach(consumer);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends Recipe<?>> void consumeTypedRecipes(Consumer<RecipeHolder<?>> consumer, RecipeType<?> type) {
        List<? extends RecipeHolder<?>> map = Minecraft.getInstance()
                .getConnection()
                .getRecipeManager().getAllRecipesFor((RecipeType) type);
        if (!map.isEmpty())
            map.forEach(consumer);
    }

    public static List<RecipeHolder<?>> getTypedRecipes(RecipeType<?> type) {
        List<RecipeHolder<?>> recipes = new ArrayList<>();
        consumeTypedRecipes(recipes::add, type);
        return recipes;
    }

    public static List<RecipeHolder<?>> getTypedRecipesExcluding(RecipeType<?> type, Predicate<RecipeHolder<?>> exclusionPred) {
        List<RecipeHolder<?>> recipes = getTypedRecipes(type);
        recipes.removeIf(exclusionPred);
        return recipes;
    }

    public static boolean doInputsMatch(Recipe<?> recipe1, Recipe<?> recipe2) {
        if (recipe1.getIngredients()
                .isEmpty()
                || recipe2.getIngredients()
                .isEmpty()) {
            return false;
        }
        ItemStack[] matchingStacks = recipe1.getIngredients()
                .getFirst()
                .getItems();
        if (matchingStacks.length == 0) {
            return false;
        }
        return recipe2.getIngredients()
                .getFirst()
                .test(matchingStacks[0]);
    }

    public static boolean doOutputsMatch(Recipe<?> recipe1, Recipe<?> recipe2) {
        RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess();
        return ItemHelper.sameItem(recipe1.getResultItem(registryAccess), recipe2.getResultItem(registryAccess));
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        CreateIceAgeJEI.runtime = runtime;
    }
}
