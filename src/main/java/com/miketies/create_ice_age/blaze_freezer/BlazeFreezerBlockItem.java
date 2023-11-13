package com.miketies.create_ice_age.blaze_freezer;

import com.miketies.create_ice_age.block.IABlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public class BlazeFreezerBlockItem extends BlockItem {
    public BlazeFreezerBlockItem(Properties pProperties) {
        super(IABlocks.BLAZE_FREEZER.get(), pProperties);
    }

    @Override
    public void registerBlocks(Map<Block, Item> pBlockToItemMap, Item pItem) {
        super.registerBlocks(pBlockToItemMap, pItem);
    }
}
