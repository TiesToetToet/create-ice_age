package com.tiestoettoet.create_ice_age.content.processing.super_freezer.breeze_freezer;

import com.simibubi.create.content.equipment.bell.BasicParticleData;
import com.simibubi.create.content.equipment.bell.CustomRotationParticle;
import com.tiestoettoet.create_ice_age.AllParticleTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

public class SnowSuperParticle extends TextureSheetParticle {
    private final float initialSize;

    protected SnowSuperParticle(ClientLevel level,
                           double x, double y, double z,
                           double xd, double yd, double zd,
                           SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd);

        this.xd = xd;
        this.yd = yd;
        this.zd = zd;

        this.lifetime = 40;
        this.initialSize = 0.125f;
        this.quadSize = initialSize;

        pickSprite(sprites);
    }

    @Override
    public void tick() {
        super.tick();

        float progress = (float) age / lifetime;

        // Shrink over lifetime
        quadSize = initialSize * (1.0f - progress);

        // Fade out
        alpha = 1.0f - progress;

        // Slow down
        xd *= 0.95;
        yd *= 0.95;
        zd *= 0.95;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Factory(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type,
                                       ClientLevel level,
                                       double x, double y, double z,
                                       double xd, double yd, double zd) {
            return new SnowSuperParticle(level, x, y, z, xd, yd, zd, sprites);
        }
    }


}