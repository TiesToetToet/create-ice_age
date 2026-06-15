package com.tiestoettoet.create_ice_age.infrastructure.data;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.TagGen;
import com.simibubi.create.infrastructure.data.CreateRegistrateTags;
import com.tiestoettoet.create_ice_age.AllTags;
import com.tiestoettoet.create_ice_age.CreateIceAge;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.Tags;

public class CreateIceAgeRegistrateTags {
    private static final CreateRegistrate REGISTRATE = CreateIceAge.registrate();

    public static void addGenerators() {
        REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, CreateIceAgeRegistrateTags::genEntityTags);
    }

    private static void genEntityTags(RegistrateTagsProvider<EntityType<?>> provIn) {
        TagGen.CreateTagsProvider<EntityType<?>> prov = new TagGen.CreateTagsProvider<>(provIn, EntityType::builtInRegistryHolder);

        prov.tag(AllTags.AllEntityTags.BLAZE_FREEZER_CAPTURABLE.tag)
                .add(EntityType.BREEZE);
    }
}
