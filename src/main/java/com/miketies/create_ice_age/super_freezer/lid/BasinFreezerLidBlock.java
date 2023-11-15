package com.miketies.create_ice_age.super_freezer.lid;

import com.miketies.create_ice_age.block.IABlockEntities;
import com.miketies.create_ice_age.item.IAItems;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BasinFreezerLidBlock extends Block implements IBE<BasinFreezerLidBlockEntity> {
    public BasinFreezerLidBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Class<BasinFreezerLidBlockEntity> getBlockEntityClass() {
        return BasinFreezerLidBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BasinFreezerLidBlockEntity> getBlockEntityType() {
        return IABlockEntities.BASIN_FREEZER_LID.get();
    }
}
