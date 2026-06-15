package com.miketies.create_ice_age.compat.jade;

import com.miketies.create_ice_age.super_freezer.lid.BasinFreezerLidBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;

public enum BasinFreezerLidProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;
    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        CompoundTag compoundTag = blockAccessor.getServerData();
        int freezeTime = compoundTag.getInt("freezeTime");
        if (freezeTime > 0) {
            iTooltip.add(Component.translatable("create_ice_age.jade.string", freezeTime));
            iTooltip.add(IThemeHelper.get().seconds(freezeTime));
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        BasinFreezerLidBlockEntity be = (BasinFreezerLidBlockEntity) blockAccessor.getBlockEntity();
        compoundTag.putInt("freezeTime", be.freezingTime);
    }

    @Override
    public ResourceLocation getUid() {
        return IAPlugin.BASIN_FREEZER_LID;
    }
}
