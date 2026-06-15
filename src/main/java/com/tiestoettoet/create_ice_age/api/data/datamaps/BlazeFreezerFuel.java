package com.tiestoettoet.create_ice_age.api.data.datamaps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record BlazeFreezerFuel(int freezeTime) {
    public static final Codec<BlazeFreezerFuel> FREEZE_TIME_CODEC = ExtraCodecs.POSITIVE_INT
            .xmap(BlazeFreezerFuel::new, BlazeFreezerFuel::freezeTime);
    public static final Codec<BlazeFreezerFuel> CODEC = Codec.withAlternative(
            RecordCodecBuilder.create(i -> i.group(
                    ExtraCodecs.POSITIVE_INT.fieldOf("freeze_time").forGetter(BlazeFreezerFuel::freezeTime)
            ).apply(i, BlazeFreezerFuel::new)),
            FREEZE_TIME_CODEC
    );
}
