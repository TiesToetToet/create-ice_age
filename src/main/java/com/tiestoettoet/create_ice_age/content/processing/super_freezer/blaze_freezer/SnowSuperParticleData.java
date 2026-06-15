package com.tiestoettoet.create_ice_age.content.processing.super_freezer.blaze_freezer;

import com.simibubi.create.content.equipment.bell.BasicParticleData;
import com.tiestoettoet.create_ice_age.AllParticleTypes;
import net.minecraft.core.particles.ParticleType;

public class SnowSuperParticleData extends BasicParticleData<SnowSuperParticle> {
    @Override
    public IBasicParticleFactory<SnowSuperParticle> getBasicFactory() {
        return (world, x, y, z, vx, vy, vz, sprites) ->
                new SnowSuperParticle(world, x, y, z, vx, vy, vz, sprites);
    }

    @Override
    public ParticleType<?> getType() {
        return AllParticleTypes.SNOW_SUPER.get();
    }
}
