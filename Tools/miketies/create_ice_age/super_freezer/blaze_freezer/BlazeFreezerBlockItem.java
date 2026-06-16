package com.miketies.create_ice_age.super_freezer.breeze_freezer;

import com.miketies.create_ice_age.block.IABlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public class BreezeFreezerBlockItem extends BlockItem {
    public BreezeFreezerBlockItem(Properties pProperties) {
        super(IABlocks.BLAZE_FREEZER.get(), pProperties);
    }

    @Override
    public void registerBlocks(Map<Block, Item> pBlockToItemMap, Item pItem) {
        super.registerBlocks(pBlockToItemMap, pItem);
    }
}
