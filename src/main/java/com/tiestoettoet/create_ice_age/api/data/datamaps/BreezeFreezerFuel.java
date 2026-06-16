package com.tiestoettoet.create_ice_age.api.data.datamaps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record BreezeFreezerFuel(int freezeTime) {
    public static final Codec<BreezeFreezerFuel> FREEZE_TIME_CODEC = ExtraCodecs.POSITIVE_INT
            .xmap(BreezeFreezerFuel::new, BreezeFreezerFuel::freezeTime);
    public static final Codec<BreezeFreezerFuel> CODEC = Codec.withAlternative(
            RecordCodecBuilder.create(i -> i.group(
                    ExtraCodecs.POSITIVE_INT.fieldOf("freeze_time").forGetter(BreezeFreezerFuel::freezeTime)
            ).apply(i, BreezeFreezerFuel::new)),
            FREEZE_TIME_CODEC
    );
}
