package com.tiestoettoet.create_ice_age.content.processing.recipe;

import com.mojang.serialization.Codec;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.breeze_freezer.BreezeFreezerBlock;
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

    public boolean testBreezeFreezer(BreezeFreezerBlock.FreezingLevel level) {
        if (this == SUPER_FREEZING)
            return level == BreezeFreezerBlock.FreezingLevel.SUPER_FREEZING;
        if (this == FREEZING)
            return level == BreezeFreezerBlock.FreezingLevel.FREEZING;
        return true;
    }

    public BreezeFreezerBlock.FreezingLevel visualizeAsBreezeFreezer() {
        if (this == SUPER_FREEZING)
            return BreezeFreezerBlock.FreezingLevel.SUPER_FREEZING;
        if (this == FREEZING)
            return BreezeFreezerBlock.FreezingLevel.FREEZING;
        return BreezeFreezerBlock.FreezingLevel.NONE;
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
