package com.miketies.create_ice_age.super_freezer.lid;

import com.miketies.create_ice_age.CreateIceAge;
import com.miketies.create_ice_age.IARecipeTypes;
import com.miketies.create_ice_age.super_freezer.blaze_freezer.BlazeFreezerBlock;
import com.miketies.create_ice_age.super_freezer.blaze_freezer.BlazeFreezerBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.List;
import java.util.Optional;

public class BasinFreezerLidBlockEntity extends BasinOperatingBlockEntity {
    public int freezingTime;
    public int recipeCooldown;
    public boolean running;
    private static final Object BASIN_FREEZER_RECIPE_KEY = new Object();
    public BasinFreezerLidBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putInt("FreezingTime", this.freezingTime);
        compound.putBoolean("Running", this.running);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.freezingTime = compound.getInt("FreezingTime");
        this.running = compound.getBoolean("Running");
    }

    @Override
    protected boolean isRunning() {
        return this.running;
    }

    @Override
    protected void onBasinRemoved() {
        if (!this.running) return;
        this.freezingTime = 0;
        this.currentRecipe = null;
        this.running = false;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getLevel().isClientSide && (this.currentRecipe == null || this.freezingTime == -1)) {
            this.recipeCooldown--;
            if (this.recipeCooldown <= 0) {
                this.running = false;
                this.freezingTime = -1;
                this.basinChecker.scheduleUpdate();
                this.recipeCooldown = 20;
            }
        } else {
            this.recipeCooldown = 0;
        }

        if (this.running && this.level != null) {
            if (this.getLevel().isClientSide && this.freezingTime > 0) {
                // TODO: Add particles
            }
            if (!this.getLevel().isClientSide && this.freezingTime <= 0) {
//                CreateIceAge.LOGGER.info("done freezing");
//                CreateIceAge.LOGGER.info(String.valueOf(this.currentRecipe));
//                CreateIceAge.LOGGER.info(String.valueOf(this.currentRecipe.getResultItem(null)));
                CreateIceAge.LOGGER.info(String.valueOf(freezingTime));
                CreateIceAge.LOGGER.info(String.valueOf(recipeCooldown));

                this.freezingTime = -1;
                this.applyBasinRecipe();
                this.sendData();
            }

            if (this.freezingTime > 0) {
                this.freezingTime--;
            }

//            if (this.freezingTime == 0) {
//                this.running = false;
//                this.basinChecker.scheduleUpdate();
//            }
        }
    }

    @Override
    protected boolean updateBasin() {
        if (this.running) return true;
        if (this.getLevel() == null || this.getLevel().isClientSide) return true;
        Optional<BasinBlockEntity> basin = this.getBasin();
        // get block below basin
        var blazeFreezer = level.getBlockEntity(worldPosition.below().below());
        if (blazeFreezer instanceof BlazeFreezerBlockEntity bf) {
            if (bf.getRemainingFreezeTime() <= 0) {
                return false;
            }
        }
        if (!this.getBasin().filter(BasinBlockEntity::canContinueProcessing).isPresent()) return true;

        List<Recipe<?>> recipes = this.getMatchingRecipes();
        if (recipes.isEmpty()) return true;
        this.currentRecipe = recipes.get(0);
        this.startProcessingBasin();
        this.sendData();
        return true;
    }

    @Override
    public void startProcessingBasin() {
        if (this.running && this.freezingTime > 0) return;
        super.startProcessingBasin();
        this.running = true;
        this.freezingTime = this.currentRecipe instanceof ProcessingRecipe<?> processed ? processed.getProcessingDuration() : 20;
//        CreateIceAge.LOGGER.info("startProcessingBasin time: "+this.freezingTime);
    }

    @Override
    protected <C extends Container> boolean matchStaticFilters(Recipe<C> recipe) {
        return recipe.getType() == IARecipeTypes.SUPER_FREEZING.getType();
    }

    @Override
    protected Object getRecipeCacheKey() {
        return BASIN_FREEZER_RECIPE_KEY;
    }

    @Override
    protected Optional<BasinBlockEntity> getBasin() {
        return this.getLevel() != null && this.getLevel().getBlockEntity(this.worldPosition.below()) instanceof BasinBlockEntity basin ? Optional.of(basin) : Optional.empty();
    }
}
