package com.tiestoettoet.create_ice_age.foundation.mixin;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlockItem;
import com.tiestoettoet.create_ice_age.AllBlocks;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.blaze_freezer.BlazeFreezerBlockItem;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = BlazeBurnerBlockItem.class)
public class BlazeBurnerBlockItemMixin extends BlockItem {
    private final CaptureState captured;

    @Overwrite
    public static BlazeBurnerBlockItem empty(Properties properties) {
        return new BlazeBurnerBlockItem(AllBlocks.BLAZE_FREEZER.get(), properties, CaptureState.EMPTY);
    }

    @Overwrite
    public static BlazeFreezerBlockItem withBlaze(Block block, Properties properties) {
        return new BlazeFreezerBlockItem(block, properties, BlazeFreezerBlockItem.CaptureState.BLAZE);
    }

    public static BlazeFreezerBlockItem withFreeze(Block block, Properties properties) {
        return new BlazeFreezerBlockItem(block, properties, BlazeFreezerBlockItem.CaptureState.FREEZE);
    }

    @Ove rwrite
    private BlazeBurnerBlockItem(Block block, Properties properties, CaptureState captured) {
        super(block, properties);
        this.captured = captured;
    }

    @Overwrite
    public boolean hasCapturedBlaze() {
        return captured == CaptureState.BLAZE || captured == CaptureState.FREEZE;
    }

    private enum CaptureState implements StringRepresentable {
        EMPTY, BLAZE, FREEZE;

        @Override
        public String getSerializedName() {
            return switch (this) {
                case EMPTY -> "empty";
                case BLAZE -> "blaze";
                case FREEZE -> "freeze";
            };
        }
    }

}
