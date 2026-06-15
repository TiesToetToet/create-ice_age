package com.miketies.create_ice_age.aaaaaaaaaa;

import com.miketies.create_ice_age.CreateIceAge;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.event.TextureStitchEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IAStichedSprite {
    private static final Map<ResourceLocation, List<IAStichedSprite>> ALL = new HashMap<>();

    protected final ResourceLocation atlasLocation;
    protected final ResourceLocation location;
    protected TextureAtlasSprite sprite;

    public IAStichedSprite(ResourceLocation atlas, ResourceLocation location) {
        atlasLocation = atlas;
        this.location = location;
        ALL.computeIfAbsent(atlasLocation, $ -> new ArrayList<>()).add(this);
        CreateIceAge.LOGGER.info("IAStichedSprite.IAStichedSprite: atlas = " + atlas + ", location = " + location);
        CreateIceAge.LOGGER.info("IAStichedSprite.IAStichedSprite: " + ALL);
    }

    public IAStichedSprite(ResourceLocation location) {
        this(InventoryMenu.BLOCK_ATLAS, location);
        CreateIceAge.LOGGER.info("IAStichedSprite.IAStichedSprite: location = " + ResourceLocation.read(String.valueOf(location)));
    }

    public static void onTextureStitchPost(TextureStitchEvent.Post event) {
        CreateIceAge.LOGGER.info("IAStichedSprite.onTextureStitchPost: event = " + event);
        CreateIceAge.LOGGER.info("IAStichedSprite.onTextureStitchPost: event.getAtlas() = " + event.getAtlas());
        TextureAtlas atlas = event.getAtlas();
        ResourceLocation atlasLocation = atlas.location();
        CreateIceAge.LOGGER.info("IAStichedSprite.onTextureStitchPost: ALL = " + ALL);
//        CreateIceAge.LOGGER.info("IAStichedSprite.onTextureStitchPost: help = " + atlas.getTextureLocations());
        List<IAStichedSprite> sprites = ALL.get(atlasLocation);
        CreateIceAge.LOGGER.info("IAStichedSprite.onTextureStitchPost: atlasLocation = " + atlasLocation + ", sprites = " + sprites);
        if (sprites != null) {
            for (IAStichedSprite sprite : sprites) {
                sprite.loadSprite(atlas);
            }
        }
    }

//    public static void onTextureStichPost() {
//        TextureAtlas atlas =
//    }

    protected void loadSprite(TextureAtlas atlas) {
        sprite = atlas.getSprite(location);
    }

    public ResourceLocation getAtlasLocation() {
        return atlasLocation;
    }

    public ResourceLocation getLocation() {
        return location;
    }

    public TextureAtlasSprite get() {
        TextureAtlas atlas = new TextureAtlas(atlasLocation);
        CreateIceAge.LOGGER.info("IAStichedSprite.get: atlasssss = " + atlas.getTextureLocations());
        CreateIceAge.LOGGER.info("IAStichedSprite.get: locationnnn = " + location);
        sprite = atlas.getSprite(location);
        CreateIceAge.LOGGER.info("IAStichedSprite.get: spriteeee = " + sprite);
        return sprite;
    }

    @Override
    public String toString() {
        return "IAStichedSprite{" +
                "atlasLocation=" + atlasLocation +
                ", location=" + location +
                ", sprite=" + sprite +
                '}';
    }
}
