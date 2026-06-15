package com.tiestoettoet.create_ice_age.content.processing.recipe;

import com.mojang.serialization.Codec;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.blaze_freezer.BlazeFreezerBlock;
import io.netty.buffer.ByteBuf;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.createmod.catnip.lang.Lang;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum FreezeCondition implements StringRepresentable {
    NONE(0xffffff), FREEZING(0x99ccff), SUPER_FREEZING(0x66ccff);

    private int color;

    public static final Codec<FreezeCondition> CODEC = StringRepresentable.fromEnum(FreezeCondition::values);
    public static final StreamCodec<ByteBuf, FreezeCondition> STREAM_CODEC = CatnipStreamCodecBuilders.ofEnum(FreezeCondition.class);

    FreezeCondition(int color) { this.color = color; }

    public boolean testBlazeFreezer(BlazeFreezerBlock.FreezingLevel level) {
        if (this == SUPER_FREEZING)
            return level == BlazeFreezerBlock.FreezingLevel.SUPER_FREEZING;
        if (this == FREEZING)
            return level == BlazeFreezerBlock.FreezingLevel.FREEZING;
        return true;
    }

    public BlazeFreezerBlock.FreezingLevel visualizeAsBlazeFreezer() {
        if (this == SUPER_FREEZING)
            return BlazeFreezerBlock.FreezingLevel.SUPER_FREEZING;
        if (this == FREEZING)
            return BlazeFreezerBlock.FreezingLevel.FREEZING;
        return BlazeFreezerBlock.FreezingLevel.NONE;
    }

    @Override
    public @NotNull String getSerializedName() {
        return Lang.asId(name());
    }

    public String getTranslationKey() {
        return "recipe.freeze_requirement." + getSerializedName();
    }

    public int getColor() {
        return color;
    }
}
