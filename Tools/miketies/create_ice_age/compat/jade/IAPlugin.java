package com.miketies.create_ice_age.compat.jade;

import com.miketies.create_ice_age.CreateIceAge;
import com.miketies.create_ice_age.super_freezer.breeze_freezer.BreezeFreezerBlock;
import com.miketies.create_ice_age.super_freezer.breeze_freezer.BreezeFreezerBlockEntity;
import com.miketies.create_ice_age.super_freezer.lid.BasinFreezerLidBlock;
import com.miketies.create_ice_age.super_freezer.lid.BasinFreezerLidBlockEntity;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin(CreateIceAge.MOD_ID)
public class IAPlugin implements IWailaPlugin {
    public static final ResourceLocation BLAZE_FREEZER = CreateIceAge.asResource("breeze_freezer");
    public static final ResourceLocation BASIN_FREEZER_LID = CreateIceAge.asResource("basin_freezer_lid");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(BreezeFreezerProvider.INSTANCE, BreezeFreezerBlockEntity.class);
        registration.registerBlockDataProvider(BasinFreezerLidProvider.INSTANCE, BasinFreezerLidBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(BreezeFreezerProvider.INSTANCE, BreezeFreezerBlock.class);
        registration.registerBlockComponent(BasinFreezerLidProvider.INSTANCE, BasinFreezerLidBlock.class);

    }
}
