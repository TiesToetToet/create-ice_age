package com.tiestoettoet.create_ice_age.foundation.mixin;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.simple.DeferralBehaviour;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import com.simibubi.create.foundation.recipe.trie.AbstractVariant;
import com.simibubi.create.foundation.recipe.trie.RecipeTrie;
import com.simibubi.create.foundation.recipe.trie.RecipeTrieFinder;
import com.tiestoettoet.create_ice_age.CreateIceAge;
import com.tiestoettoet.create_ice_age.content.processing.basin.CreateIceAgeBasinBlockEntity;
import com.tiestoettoet.create_ice_age.content.processing.basin.CreateIceAgeBasinRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Mixin(value = BasinOperatingBlockEntity.class)
public abstract class BasinOperatingBlockEntityMixin extends KineticBlockEntity {

    @Shadow
    public DeferralBehaviour basinChecker;
    @Shadow
    protected Recipe<?> currentRecipe;

    public BasinOperatingBlockEntityMixin(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Overwrite
    protected boolean updateBasin() {
        if (!isSpeedRequirementFulfilled())
            return true;
        if (getSpeed() == 0)
            return true;
        if (isRunning())
            return true;
        if (level == null || level.isClientSide)
            return true;
        Optional<BasinBlockEntity> basin = getBasin();
        if (!basin.filter(BasinBlockEntity::canContinueProcessing)
                .isPresent())
            return true;

        List<Recipe<?>> recipes = getMatchingRecipes();
        if (recipes.isEmpty())
            return true;
        currentRecipe = recipes.get(0);
        startProcessingBasin();
        sendData();
        return true;
    }

    @Shadow
    protected abstract boolean isRunning();

    @Shadow
    public void startProcessingBasin() {
    }

    @Shadow
    public boolean continueWithPreviousRecipe() {
        return true;
    }

    @Overwrite
    protected <I extends RecipeInput> boolean matchBasinRecipe(Recipe<I> recipe) {
        if (recipe == null)
            return false;
        Optional<BasinBlockEntity> basin = getBasin();
        if (!basin.isPresent())
            return false;
        return CreateIceAgeBasinRecipe.match(basin.get(), recipe);
    }

    @Overwrite
    protected void applyBasinRecipe() {
        if (this.currentRecipe != null) {
            Optional<BasinBlockEntity> optionalBasin = this.getBasin();
            if (optionalBasin.isPresent()) {
                BasinBlockEntity basin = (BasinBlockEntity)optionalBasin.get();
                boolean wasEmpty = basin.canContinueProcessing();
                if (CreateIceAgeBasinRecipe.apply(basin, this.currentRecipe)) {
                    this.getProcessedRecipeTrigger().ifPresent(this::award);
                    basin.inputTank.sendDataImmediately();
                    if (wasEmpty && this.matchBasinRecipe(this.currentRecipe)) {
                        this.continueWithPreviousRecipe();
                        this.sendData();
                    }

                    basin.notifyChangeOfContents();
                }
            }
        }
        }

    @Overwrite
    protected List<Recipe<?>> getMatchingRecipes() {
        Optional<BasinBlockEntity> $basin = getBasin();
        BasinBlockEntity basin;
        if ($basin.isEmpty() || (basin = $basin.get()).isEmpty())
            return new ArrayList<>();

        List<Recipe<?>> list = new ArrayList<>();
        try {

            IItemHandler availableItems = level.getCapability(Capabilities.ItemHandler.BLOCK, basin.getBlockPos(), null);
            IFluidHandler availableFluids = level.getCapability(Capabilities.FluidHandler.BLOCK, basin.getBlockPos(), null);

            // no point even searching, since no recipe will ever match
            if (availableItems == null && availableFluids == null) {
                return list;
            }

            RecipeTrie<?> trie = RecipeTrieFinder.get(getRecipeCacheKey(), level, this::matchStaticFilters);
            Set<AbstractVariant> availableVariants = RecipeTrie.getVariants(availableItems, availableFluids);

            for (Recipe<?> r : trie.lookup(availableVariants))
                if (matchBasinRecipe(r))
                    list.add(r);
        } catch (Exception e) {
            CreateIceAge.LOGGER.error("Failed to get recipe trie, falling back to slow logic", e);
            list.clear();

            for (RecipeHolder<? extends Recipe<?>> r : RecipeFinder.get(getRecipeCacheKey(), level, this::matchStaticFilters))
                if (matchBasinRecipe(r.value()))
                    list.add(r.value());
        }

        list.sort((r1, r2) -> r2.getIngredients().size() - r1.getIngredients().size());

        return list;
    }

    @Overwrite
    protected Optional<BasinBlockEntity> getBasin() {
        if (level == null)
            return Optional.empty();
        BlockEntity basinBE = level.getBlockEntity(worldPosition.below(2));
        if (!(basinBE instanceof BasinBlockEntity))
            return Optional.empty();
        return Optional.of((BasinBlockEntity) basinBE);
    }

    @Shadow
    protected Optional<CreateAdvancement> getProcessedRecipeTrigger() {
        return Optional.empty();
    }

    @Shadow
    protected abstract boolean matchStaticFilters(RecipeHolder<? extends Recipe<?>> recipe);

    @Shadow
    protected abstract Object getRecipeCacheKey();


}
