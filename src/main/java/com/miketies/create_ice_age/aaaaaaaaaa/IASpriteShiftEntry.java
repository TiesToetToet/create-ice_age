package com.miketies.create_ice_age.aaaaaaaaaa;

import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

public class IASpriteShiftEntry extends SpriteShiftEntry {
    protected IAStichedSprite original;
    protected IAStichedSprite target;

    public void set(ResourceLocation originalTextureLocation, ResourceLocation targetTextureLocation) {
        original = new IAStichedSprite(originalTextureLocation);
        target = new IAStichedSprite(targetTextureLocation);
    }

    public ResourceLocation getOriginalResourceLocation() {
        return original.getLocation();
    }

    public ResourceLocation getTargetResourceLocation() {
        return target.getLocation();
    }

    public TextureAtlasSprite getOriginal() {
        return original.get();
    }

    public TextureAtlasSprite getTarget() {
        return target.get();
    }

    public float getTargetU(float localU) {
        return getTarget().getU(getUnInterpolatedU(getOriginal(), localU));
    }

    public float getTargetV(float localV) {
        return getTarget().getV(getUnInterpolatedV(getOriginal(), localV));
    }

    public static float getUnInterpolatedU(TextureAtlasSprite sprite, float u) {
        float f = sprite.getU1() - sprite.getU0();
        return (u - sprite.getU0()) / f * 16.0F;
    }

    public static float getUnInterpolatedV(TextureAtlasSprite sprite, float v) {
        float f = sprite.getV1() - sprite.getV0();
        return (v - sprite.getV0()) / f * 16.0F;
    }

    @Override
    public String toString() {
        return "IASpriteShiftEntry{" +
                "original=" + original +
                ", target=" + target +
                '}';
    }
}
