package com.tiestoettoet.create_ice_age.content.processing.super_freezer.breeze_freezer;

import com.simibubi.create.content.equipment.bell.BasicParticleData;
import com.tiestoettoet.create_ice_age.AllParticleTypes;
import net.minecraft.core.particles.ParticleType;

public class SnowParticleData extends BasicParticleData<SnowParticle> {
    @Override
    public IBasicParticleFactory<SnowParticle> getBasicFactory() {
        return (world, x, y, z, vx, vy, vz, sprites) ->
                new SnowParticle(world, x, y, z, vx, vy, vz, sprites);
    }

    @Override
    public ParticleType<?> getType() {
        return AllParticleTypes.SNOW.get();
    }
}
