package com.miketies.create_ice_age.super_freezer.lid;

import com.miketies.create_ice_age.block.IABlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public class BasinFreezerLidBlockItem extends BlockItem {
    public BasinFreezerLidBlockItem(Properties pProperties) {
        super(IABlocks.BASIN_FREEZER_LID.get(), pProperties);
    }

    @Override
    public void registerBlocks(Map<Block, Item> pBlockToItemMap, Item pItem) {
        super.registerBlocks(pBlockToItemMap, pItem);
    }
}
