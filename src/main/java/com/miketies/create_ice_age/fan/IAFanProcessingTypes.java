package com.miketies.create_ice_age.fan;

import com.miketies.create_ice_age.CreateIceAge;
import com.miketies.create_ice_age.IARecipeTypes;
import com.miketies.create_ice_age.fluid.IAFluidType;
import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingTypeRegistry;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class IAFanProcessingTypes {
    public static final FanProcessingType FREEZING = register("freezing", new FreezingType());

    private static <T extends FanProcessingType> T register(String id, T type) {
        FanProcessingTypeRegistry.register(CreateIceAge.asResource(id), type);
        return type;
    }

    public static void register() {
    }

    public static class FreezingType implements FanProcessingType {
        private static final FreezingRecipe.FreezingWrapper WRAPPER = new FreezingRecipe.FreezingWrapper();

        @Override
        public boolean isValidAt(Level level, BlockPos pos) {
            FluidState fluidState = level.getFluidState(pos);
            if(fluidState.getType().getFluidType().equals(IAFluidType.LIQUID_ICE_FLUID_TYPE.get())) {
                return true;
            }
            return false;
        }

        @Override
        public int getPriority() {
            return 500;
        }

        @Override
        public boolean canProcess(ItemStack stack, Level level) {
            WRAPPER.setItem(0, stack);
            Optional<FreezingRecipe> recipe = IARecipeTypes.FREEZING.find(WRAPPER, level);
            return recipe.isPresent();
        }

        @Override
        public @Nullable List<ItemStack> process(ItemStack stack, Level level) {
            WRAPPER.setItem(0, stack);
            Optional<FreezingRecipe> recipe = IARecipeTypes.FREEZING.find(WRAPPER, level);
            if(recipe.isPresent()) {
                return RecipeApplier.applyRecipeOn(level, stack, recipe.get());
            }
            return null;
        }

        @Override
        public void spawnProcessingParticles(Level level, Vec3 pos) {
            if(level.random.nextInt(8) != 0) {
                return;
            }
            pos = pos.add(VecHelper.offsetRandomly(Vec3.ZERO, level.random, 1)
                    .multiply(1, 0.05f, 1)
                    .normalize()
                    .scale(0.15f));
            level.addParticle(ParticleTypes.ITEM_SNOWBALL, pos.x, pos.y + 0.45f, pos.z, 0, 0, 0);
            if(level.random.nextInt(2) == 0) {
                level.addParticle(ParticleTypes.SNOWFLAKE, pos.x, pos.y + 0.25f, pos.z, 0, 0, 0);
            }
        }

        @Override
        public void morphAirFlow(AirFlowParticleAccess particleAccess, RandomSource random) {
            particleAccess.setColor(Color.mixColors(0x20c3d0, 0xc2f1f2, random.nextFloat()));
            particleAccess.setAlpha(1f);
            if (random.nextFloat() < 1 / 128f)
                particleAccess.spawnExtraParticle(ParticleTypes.ITEM_SNOWBALL, .125f);
            if (random.nextFloat() < 1 / 32f)
                particleAccess.spawnExtraParticle(ParticleTypes.SNOWFLAKE, .125f);
        }

        @Override
        public void affectEntity(Entity entity, Level level) {
            if(entity instanceof LivingEntity livingEntity) {
                if(livingEntity.canFreeze()) {
                    // freeze the entity for 11 seconds, of which 4 are being actually frozen
                    livingEntity.setTicksFrozen(220);
                }
            }
        }
    }
}
