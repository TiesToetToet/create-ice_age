package com.miketies.create_ice_age.compat.jade;

import com.miketies.create_ice_age.CreateIceAge;
import com.miketies.create_ice_age.blaze_freezer.BlazeFreezerBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.IElementHelper;

//@WailaPlugin(CreateIceAge.MOD_ID)
public enum BlazeFreezerProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;
    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        CompoundTag compoundTag = blockAccessor.getServerData();
        int remainingFreezeTime = compoundTag.getInt("remainingFreezeTime");
        if (remainingFreezeTime > 0) {
            tooltip.add(IElementHelper.get().smallItem(new ItemStack(Items.ICE)));
            tooltip.append(IThemeHelper.get().seconds(compoundTag.getInt("remainingFreezeTime")));
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        BlazeFreezerBlockEntity be = (BlazeFreezerBlockEntity) blockAccessor.getBlockEntity();
        compoundTag.putInt("remainingFreezeTime", be.getRemainingFreezeTime());
    }

    @Override
    public ResourceLocation getUid() {
        return IAPlugin.BLAZE_FREEZER;
    }
}
