package com.tiestoettoet.create_ice_age.infrastructure.ponder.scenes;

import com.simibubi.create.content.kinetics.deployer.DeployerBlockEntity;
import com.tiestoettoet.create_ice_age.AllItems;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.breeze_freezer.BreezeFreezerBlock;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class ProcessingScenes {
    public static void emptyBlazeBurner(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("empty_blaze_burner", "Using Empty Blaze Burners");
        scene.configureBasePlate(0, 0, 5);
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(10);
        BlockPos center = util.grid().at(2, 0, 2);

        scene.world().createEntity(w -> {
            Breeze breezeEntity = EntityType.BREEZE.create(w);
            Vec3 v = util.vector().topOf(center);
            breezeEntity.setPosRaw(v.x, v.y, v.z);
            breezeEntity.setYRot(breezeEntity.yRotO = 180);
            return breezeEntity;
        });

        scene.idle(20);
        scene.overlay().showControls(util.vector().centerOf(center.above(2)), Pointing.DOWN, 40).rightClick()
                .withItem(com.simibubi.create.AllItems.EMPTY_BLAZE_BURNER.asStack());
        scene.idle(10);
        scene.overlay().showText(60)
                .text("Right-click a Breeze with the empty burner to capture it")
                .attachKeyFrame()
                .pointAt(util.vector().blockSurface(center.above(2), Direction.WEST))
                .placeNearTarget();
        scene.idle(50);

        scene.world().modifyEntities(Breeze.class, Entity::discard);
        scene.idle(20);

        scene.world().showSection(util.select().position(2, 1, 2), Direction.DOWN);
        scene.idle(20);
        scene.overlay().showControls(util.vector().topOf(center.above()), Pointing.DOWN, 40).rightClick()
                .withItem(com.simibubi.create.AllItems.EMPTY_BLAZE_BURNER.asStack());
        scene.idle(10);
        scene.overlay().showText(60)
                .text("Alternatively, Breezes can be collected from their Spawners directly")
                .attachKeyFrame()
                .pointAt(util.vector().blockSurface(center.above(), Direction.WEST))
                .placeNearTarget();
        scene.idle(50);
        scene.world().hideSection(util.select().position(2, 1, 2), Direction.UP);
        scene.idle(20);
        scene.world().showSection(util.select().position(1, 1, 2), Direction.DOWN);
        scene.idle(20);

        scene.world().modifyBlock(util.grid().at(1, 1, 2), s -> s.setValue(BreezeFreezerBlock.FREEZE_LEVEL, BreezeFreezerBlock.FreezingLevel.FREEZING),
                false);
        scene.overlay().showText(70)
                .text("You now have an ideal freeze source for various machines")
                .attachKeyFrame()
                .pointAt(util.vector().blockSurface(center.west()
                        .above(), Direction.WEST))
                .placeNearTarget();

        scene.idle(70);
    }

    public static void breezeFreezer(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("breeze_freezer", "Feeding Breeze Freezers");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
        scene.idle(10);

        BlockPos freezer = util.grid().at(2, 1, 2);
        scene.world().showSection(util.select().position(freezer), Direction.DOWN);
        scene.idle(10);
        scene.world().showSection(util.select().position(freezer.above()), Direction.DOWN);
        scene.idle(10);

        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("Breeze Freezers can provide Heat to Items processed in a Basin")
                .pointAt(util.vector().blockSurface(freezer, Direction.WEST))
                .placeNearTarget();
        scene.idle(80);

        scene.world().hideSection(util.select().position(freezer.above()), Direction.UP);
        scene.idle(20);
        scene.world().setBlock(freezer.above(), Blocks.AIR.defaultBlockState(), false);
        scene.overlay().showControls(util.vector().topOf(freezer), Pointing.DOWN, 15).rightClick()
                .withItem(new ItemStack(Items.ICE));
        scene.idle(7);
        scene.world().modifyBlock(freezer, s -> s.setValue(BreezeFreezerBlock.FREEZE_LEVEL, BreezeFreezerBlock.FreezingLevel.FREEZING), false);
        scene.idle(20);

        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("For this, the Breeze has to be fed with freezable items")
                .pointAt(util.vector().blockSurface(freezer, Direction.WEST))
                .placeNearTarget();
        scene.idle(80);

        scene.idle(20);
        scene.overlay().showControls(util.vector().topOf(freezer), Pointing.DOWN, 30).rightClick()
                .withItem(AllItems.ICE_CAKE.asStack());
        scene.idle(7);
        scene.world().modifyBlock(freezer, s -> s.setValue(BreezeFreezerBlock.FREEZE_LEVEL, BreezeFreezerBlock.FreezingLevel.SUPER_FREEZING), false);
        scene.idle(20);

        scene.overlay().showText(80)
                .attachKeyFrame()
                .colored(PonderPalette.MEDIUM)
                .text("With an Ice Cake, the Breeze can reach an even stronger level of freeze")
                .pointAt(util.vector().blockSurface(freezer, Direction.WEST))
                .placeNearTarget();
        scene.idle(90);

        Class<DeployerBlockEntity> teType = DeployerBlockEntity.class;
        scene.world().modifyBlockEntityNBT(util.select().position(4, 1, 2), teType,
                nbt -> nbt.put("HeldItem", AllItems.ICE_CAKE.asStack().saveOptional(scene.world().getHolderLookupProvider())));

        scene.world().showSection(util.select().fromTo(3, 0, 5, 2, 0, 5), Direction.UP);
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(4, 1, 2, 4, 1, 5), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(2, 1, 4, 2, 1, 5), Direction.DOWN);
        scene.idle(10);

        scene.overlay().showText(80)
                .attachKeyFrame()
                .text("The feeding process can be automated using Deployers or Mechanical Arms")
                .pointAt(util.vector().blockSurface(freezer.east(2), Direction.UP));
        scene.idle(90);
    }
}
