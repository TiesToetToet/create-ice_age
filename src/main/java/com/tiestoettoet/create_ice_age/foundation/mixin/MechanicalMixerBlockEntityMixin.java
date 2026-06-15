package com.tiestoettoet.create_ice_age.foundation.mixin;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.fluids.FluidFX;
import com.simibubi.create.content.fluids.potion.PotionMixingRecipes;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.infrastructure.config.AllConfigs;
//import com.tiestoettoet.create_ice_age.content.processing.basin.CreateIceAgeBasinBlockEntity;
import com.tiestoettoet.create_ice_age.content.processing.basin.CreateIceAgeBasinRecipe;
import com.tiestoettoet.create_ice_age.content.processing.recipe.CreateIceAgeStandardProcessingRecipe;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.blaze_freezer.BlazeFreezerBlockEntity;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Optional;

@Mixin(value = MechanicalMixerBlockEntity.class)
public class MechanicalMixerBlockEntityMixin extends BasinOperatingBlockEntity {

    protected MechanicalMixerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Shadow
    private static final Object shapelessOrMixingRecipesKey = new Object();

    @Shadow public int runningTicks;
    @Shadow public int processingTicks;
    @Shadow public boolean running;


    @Overwrite
    public void tick() {
        super.tick();

        if (runningTicks >= 40) {
            running = false;
            runningTicks = 0;
            basinChecker.scheduleUpdate();
            return;
        }

        float speed = Math.abs(getSpeed());
        if (running && level != null) {
            if (level.isClientSide && runningTicks == 20)
                renderParticles();

            if (getSpeed() == 0 || !isSpeedRequirementFulfilled()) {
                if (runningTicks < 20)
                    runningTicks = 40 - runningTicks;
                else if (runningTicks == 20)
                    runningTicks++;
            }

            if ((!level.isClientSide || isVirtual()) && runningTicks == 20) {
                if (processingTicks < 0) {
                    float recipeSpeed = 1;
                    if (currentRecipe instanceof StandardProcessingRecipe) {
                        int t = ((StandardProcessingRecipe<?>) currentRecipe).getProcessingDuration();
                        if (t != 0)
                            recipeSpeed = t / 100f;
                    }
                    if (currentRecipe instanceof CreateIceAgeStandardProcessingRecipe) {
                        int t = ((CreateIceAgeStandardProcessingRecipe<?>) currentRecipe).getProcessingDuration();
                        if (t != 0)
                            recipeSpeed = t / 100f;
                    }

                    processingTicks = Math.max((Mth.log2((int) (512 / speed))) * Mth.ceil(recipeSpeed * 15) + 1, 1);

                    Optional<BasinBlockEntity> basin = getBasin();
                    if (basin.isPresent()) {
                        Couple<SmartFluidTankBehaviour> tanks = basin.get()
                                .getTanks();
                        if (!tanks.getFirst()
                                .isEmpty()
                                || !tanks.getSecond()
                                .isEmpty())
                            level.playSound(null, worldPosition, SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_AMBIENT,
                                    SoundSource.BLOCKS, .75f, speed < 65 ? .75f : 1.5f);
                    }

                } else {
                    processingTicks--;
                    if (processingTicks == 0) {
                        runningTicks++;
                        processingTicks = -1;
                        applyBasinRecipe();
                        sendData();
                    }
                }
            }

            if (runningTicks != 20)
                runningTicks++;
        }
    }

    @Shadow
    protected boolean isRunning() {
        return running;
    }


    @Shadow
    protected void onBasinRemoved() {
        if (!running)
            return;
        runningTicks = 40;
        running = false;
    }

    @Shadow
    public void renderParticles() {
        Optional<BasinBlockEntity> basin = getBasin();
        if (!basin.isPresent() || level == null)
            return;

        for (SmartInventory inv : basin.get()
                .getInvs()) {
            for (int slot = 0; slot < inv.getSlots(); slot++) {
                ItemStack stackInSlot = inv.getItem(slot);
                if (stackInSlot.isEmpty())
                    continue;
                ItemParticleOption data = new ItemParticleOption(ParticleTypes.ITEM, stackInSlot);
                spillParticle(data);
            }
        }

        for (SmartFluidTankBehaviour behaviour : basin.get()
                .getTanks()) {
            if (behaviour == null)
                continue;
            for (SmartFluidTankBehaviour.TankSegment tankSegment : behaviour.getTanks()) {
                if (tankSegment.isEmpty(0))
                    continue;
                spillParticle(FluidFX.getFluidParticle(tankSegment.getRenderedFluid()));
            }
        }
    }

    @Shadow
    protected void spillParticle(ParticleOptions data) {
        float angle = level.random.nextFloat() * 360;
        Vec3 offset = new Vec3(0, 0, 0.25f);
        offset = VecHelper.rotate(offset, angle, Direction.Axis.Y);
        Vec3 target = VecHelper.rotate(offset, getSpeed() > 0 ? 25 : -25, Direction.Axis.Y)
                .add(0, .25f, 0);
        Vec3 center = offset.add(VecHelper.getCenterOf(worldPosition));
        target = VecHelper.offsetRandomly(target.subtract(offset), level.random, 1 / 128f);
        level.addParticle(data, center.x, center.y - 1.75f, center.z, target.x, target.y, target.z);
    }

    @Overwrite
    protected List<Recipe<?>> getMatchingRecipes() {
        List<Recipe<?>> matchingRecipes = super.getMatchingRecipes();

        if (!AllConfigs.server().recipes.allowBrewingInMixer.get())
            return matchingRecipes;

        Optional<BasinBlockEntity> basin = getBasin();
        if (!basin.isPresent())
            return matchingRecipes;

        BasinBlockEntity basinBlockEntity = basin.get();
        if (basin.isEmpty())
            return matchingRecipes;

        IItemHandler availableItems = level.getCapability(Capabilities.ItemHandler.BLOCK, basinBlockEntity.getBlockPos(), null);
        if (availableItems == null)
            return matchingRecipes;

        for (int i = 0; i < availableItems.getSlots(); i++) {
            ItemStack stack = availableItems.getStackInSlot(i);
            if (stack.isEmpty())
                continue;

            List<MixingRecipe> list = PotionMixingRecipes.sortRecipesByItem(level).get(stack.getItem());
            if (list == null)
                continue;
            for (MixingRecipe mixingRecipe : list)
                if (matchBasinRecipe(mixingRecipe))
                    matchingRecipes.add(mixingRecipe);
        }

        return matchingRecipes;
    }


    @Override
    protected <I extends RecipeInput> boolean matchBasinRecipe(Recipe<I> recipe) {
        if (recipe == null)
            return false;
        Optional<BasinBlockEntity> basin = getBasin();
        if (!basin.isPresent())
            return false;
        return CreateIceAgeBasinRecipe.match(basin.get(), recipe);
    }

//    @Override
//    protected boolean updateBasin() {
//        if (this.running) return true;
//        if (this.getLevel() == null || this.getLevel().isClientSide) return true;
//        Optional<CreateIceAgeBasinBlockEntity> basin = this.getBasin2();
//        // get block below basin
//        var blazeFreezer = level.getBlockEntity(worldPosition.below().below());
//        if (blazeFreezer instanceof BlazeFreezerBlockEntity bf) {
//            if (bf.getRemainingFreezeTime() <= 0) {
//                return false;
//            }
//        }
//        if (!this.getBasin2().filter(CreateIceAgeBasinBlockEntity::canContinueProcessing).isPresent()) return true;
//
//        List<Recipe<?>> recipes = this.getMatchingRecipes();
//        if (recipes.isEmpty()) return true;
//        this.currentRecipe = recipes.get(0);
//        this.startProcessingBasin();
//        this.sendData();
//        return true;
//    }



    @Overwrite
//    @Override
    protected boolean matchStaticFilters(RecipeHolder<? extends Recipe<?>> recipe) {
        Recipe<?> r = recipe.value();
        return ((r instanceof CraftingRecipe && !(r instanceof ShapedRecipe)
                && AllConfigs.server().recipes.allowShapelessInMixer.get() && r.getIngredients()
                .size() > 1
                && !MechanicalPressBlockEntity.canCompress(r)) && !AllRecipeTypes.shouldIgnoreInAutomation(recipe)
                || r.getType() == AllRecipeTypes.MIXING.getType())
                || r.getType() == com.tiestoettoet.create_ice_age.AllRecipeTypes.FREEZING_LID.getType()
                ;
    }

    @Shadow
    @Final
    protected Object getRecipeCacheKey() {
        return shapelessOrMixingRecipesKey;
    }
}
