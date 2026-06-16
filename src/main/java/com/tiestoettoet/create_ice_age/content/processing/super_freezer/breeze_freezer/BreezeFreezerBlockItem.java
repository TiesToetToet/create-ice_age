package com.tiestoettoet.create_ice_age.content.processing.super_freezer.breeze_freezer;

import com.tiestoettoet.create_ice_age.AllBlocks;
import com.tiestoettoet.create_ice_age.AllTags;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.registry.RegisteredObjectsHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerData;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BreezeFreezerBlockItem extends BlockItem {

    private static final Logger log = LoggerFactory.getLogger(BreezeFreezerBlockItem.class);
    private final CaptureState captured;

    public static BreezeFreezerBlockItem empty(Properties properties) {
        return new BreezeFreezerBlockItem(AllBlocks.BREEZE_FREEZER.get(), properties, CaptureState.EMPTY);
    }

    public static BreezeFreezerBlockItem withBlaze(Block block, Properties properties) {
        return new BreezeFreezerBlockItem(block, properties, CaptureState.BLAZE);
    }

    public static BreezeFreezerBlockItem withBreeze(Block block, Properties properties) {
        return new BreezeFreezerBlockItem(block, properties, CaptureState.BREEZE);
    }

    @Override
    public void registerBlocks(Map<Block, Item> p_195946_1_, Item p_195946_2_) {
        if (!hasCapturedBreeze())
            return;
        super.registerBlocks(p_195946_1_, p_195946_2_);
    }

    public BreezeFreezerBlockItem(Block block, Properties properties, CaptureState captured) {
        super(block, properties);
        this.captured = captured;
    }

    @Override
    public String getDescriptionId() {
        return hasCapturedBreeze() ? super.getDescriptionId() : "item.create." + RegisteredObjectsHelper.getKeyOrThrow(this).getPath();
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (hasCapturedBreeze())
            return super.useOn(context);

        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockEntity be = world.getBlockEntity(pos);
        Player player = context.getPlayer();

        if (be instanceof TrialSpawnerBlockEntity) {

            TrialSpawner spawner = ((TrialSpawnerBlockEntity) be).getTrialSpawner();
            TrialSpawnerData data = spawner.getData();
            Optional<SpawnData> optionalSpawnData = data.nextSpawnData;
            SpawnData spawnData;
            if (optionalSpawnData.isEmpty()) {
                log.warn("Tried to capture breeze from trial spawner at {} but it had no spawn data", pos);
                return super.useOn(context);
            } else {
                spawnData = optionalSpawnData.get();
            }


            Optional<EntityType<?>> optionalEntity = EntityType.by(spawnData.entityToSpawn());

            if (optionalEntity.isEmpty() || !AllTags.AllEntityTags.BREEZE_FREEZER_CAPTURABLE.matches(optionalEntity.get()))
                return super.useOn(context);

            spawnCaptureEffectsFreeze(world, VecHelper.getCenterOf(pos));
            if (world.isClientSide || player == null)
                return InteractionResult.SUCCESS;

            giveFreezerItemTo(player, context.getItemInHand(), context.getHand());
            return InteractionResult.SUCCESS;

        } else if (be instanceof SpawnerBlockEntity) {

            BaseSpawner spawner = ((SpawnerBlockEntity) be).getSpawner();

            List<SpawnData> possibleSpawns = spawner.spawnPotentials.unwrap()
                    .stream()
                    .map(WeightedEntry.Wrapper::data)
                    .toList();

            if (possibleSpawns.isEmpty()) {
                possibleSpawns = new ArrayList<>();
                possibleSpawns.add(spawner.nextSpawnData);
            }

            for (SpawnData e : possibleSpawns) {
                Optional<EntityType<?>> optionalEntity = EntityType.by(e.entityToSpawn());
                if (optionalEntity.isEmpty() || !com.simibubi.create.AllTags.AllEntityTags.BLAZE_BURNER_CAPTURABLE.matches(optionalEntity.get()))
                    continue;

                spawnCaptureEffects(world, VecHelper.getCenterOf(pos));
                if (world.isClientSide || player == null)
                    return InteractionResult.SUCCESS;

                giveBurnerItemTo(player, context.getItemInHand(), context.getHand());
                return InteractionResult.SUCCESS;
            }
        }

        return super.useOn(context);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack heldItem, Player player, LivingEntity entity,
                                                  InteractionHand hand) {
        if (hasCapturedBreeze())
            return InteractionResult.PASS;
        if (!AllTags.AllEntityTags.BREEZE_FREEZER_CAPTURABLE.matches(entity) && com.simibubi.create.AllTags.AllEntityTags.BLAZE_BURNER_CAPTURABLE.matches(entity))
            return InteractionResult.PASS;

        Level world = player.level();
        spawnCaptureEffects(world, entity.position());
        if (world.isClientSide)
            return InteractionResult.FAIL;

        if (AllTags.AllEntityTags.BREEZE_FREEZER_CAPTURABLE.matches(entity)) {
            giveFreezerItemTo(player, heldItem, hand);
        }
        if (com.simibubi.create.AllTags.AllEntityTags.BLAZE_BURNER_CAPTURABLE.matches(entity)) {
            giveBurnerItemTo(player, heldItem, hand);
        }
        giveBurnerItemTo(player, heldItem, hand);
        entity.discard();
        return InteractionResult.FAIL;
    }

    protected void giveBurnerItemTo(Player player, ItemStack heldItem, InteractionHand hand) {
        ItemStack filled = com.simibubi.create.AllBlocks.BLAZE_BURNER.asStack();
        if (!player.isCreative())
            heldItem.shrink(1);
        if (heldItem.isEmpty()) {
            player.setItemInHand(hand, filled);
            return;
        }
        player.getInventory()
                .placeItemBackInInventory(filled);
    }


    protected void giveFreezerItemTo(Player player, ItemStack heldItem, InteractionHand hand) {
        ItemStack filled = AllBlocks.BREEZE_FREEZER.asStack();
        if (!player.isCreative())
            heldItem.shrink(1);
        if (heldItem.isEmpty()) {
            player.setItemInHand(hand, filled);
            return;
        }
        player.getInventory()
                .placeItemBackInInventory(filled);
    }

    private void spawnCaptureEffects(Level world, Vec3 vec) {
        if (world.isClientSide) {
            for (int i = 0; i < 40; i++) {
                Vec3 motion = VecHelper.offsetRandomly(Vec3.ZERO, world.random, .125f);
                world.addParticle(ParticleTypes.FLAME, vec.x, vec.y, vec.z, motion.x, motion.y, motion.z);
                Vec3 circle = motion.multiply(1, 0, 1)
                        .normalize()
                        .scale(.5f);
                world.addParticle(ParticleTypes.SMOKE, circle.x, vec.y, circle.z, 0, -0.125, 0);
            }
            return;
        }

        BlockPos soundPos = BlockPos.containing(vec);
        world.playSound(null, soundPos, SoundEvents.BLAZE_HURT, SoundSource.HOSTILE, .25f, .75f);
        world.playSound(null, soundPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.HOSTILE, .5f, .75f);
    }

    private void spawnCaptureEffectsFreeze(Level world, Vec3 vec) {
        if (world.isClientSide) {
            for (int i = 0; i < 40; i++) {
                Vec3 motion = VecHelper.offsetRandomly(Vec3.ZERO, world.random, .125f);
                world.addParticle(ParticleTypes.ITEM_SNOWBALL, vec.x, vec.y, vec.z, motion.x, motion.y, motion.z);
                Vec3 circle = motion.multiply(1, 0, 1)
                        .normalize()
                        .scale(.25f);
                world.addParticle(ParticleTypes.SNOWFLAKE, circle.x, vec.y, circle.z, 0, -0.125, 0);
            }
            return;
        }

        BlockPos soundPos = BlockPos.containing(vec);
//        world.playSound(null, soundPos, SoundEvents.BLAZE_HURT, SoundSource.HOSTILE, .25f, .75f);
//        world.playSound(null, soundPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.HOSTILE, .5f, .75f);
    }

    public boolean hasCapturedBreeze() {
        return captured == CaptureState.BLAZE || captured == CaptureState.BREEZE;
    }

    public enum CaptureState implements StringRepresentable {
        EMPTY, BLAZE, BREEZE;

        @Override
        public String getSerializedName() {
            return switch (this) {
                case EMPTY -> "empty";
                case BLAZE -> "blaze";
                case BREEZE -> "breeze";
            };
        }
    }
}
