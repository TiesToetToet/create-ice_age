package com.miketies.create_ice_age.compat.jade;

import com.miketies.create_ice_age.CreateIceAge;
import com.miketies.create_ice_age.blaze_freezer.BlazeFreezerBlock;
import com.miketies.create_ice_age.blaze_freezer.BlazeFreezerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin(CreateIceAge.MOD_ID)
public class IAPlugin implements IWailaPlugin {
    public static final ResourceLocation BLAZE_FREEZER = CreateIceAge.asResource("blaze_freezer");

    @Override
    public void register(IWailaCommonRegistration registration) {
        System.out.println("Registering IAPlugin");
        registration.registerBlockDataProvider(BlazeFreezerProvider.INSTANCE, BlazeFreezerBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(BlazeFreezerProvider.INSTANCE, BlazeFreezerBlock.class);

    }
}
