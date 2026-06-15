package com.tiestoettoet.create_ice_age;

import com.simibubi.create.api.registry.CreateDataMaps;
import net.createmod.catnip.lang.Lang;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import static com.tiestoettoet.create_ice_age.AllTags.NameSpace.MOD;

public class AllTags {
    public enum NameSpace {
        MOD(CreateIceAge.MOD_ID);

        public final String id;

        NameSpace(String id) {
            this.id = id;
        }

        public ResourceLocation id(String path) {
            return ResourceLocation.fromNamespaceAndPath(this.id, path);
        }

        public ResourceLocation id(Enum<?> entry, @Nullable String pathOverride) {
            return this.id(pathOverride != null ? pathOverride : Lang.asId(entry.name()));
        }
    }

    public enum AllItemTags {
        /**
         * @deprecated
         */
        @ApiStatus.ScheduledForRemoval(inVersion = "1.21.1+ Port")
        FREEZE_FUEL_REGULAR(MOD, "freeze_fuel/regular"),
        /**
         * @deprecated
         */
        @ApiStatus.ScheduledForRemoval(inVersion = "1.21.1+ Port")
        FREEZE_FUEL_SPECIAL(MOD, "freeze_fuel/special");

        public final TagKey<Item> tag;

        AllItemTags() {
            this(MOD);
        }

        AllItemTags(NameSpace namespace) {
            this(namespace, null);
        }

        AllItemTags(NameSpace namespace, @Nullable String pathOverride) {
            this.tag = TagKey.create(Registries.ITEM, namespace.id(this, pathOverride));
        }

        @SuppressWarnings("deprecation")
        public boolean matches(Item item) {
            return item.builtInRegistryHolder()
                    .is(tag);
        }

        public boolean matches(ItemStack stack) {
            return stack.is(tag);
        }
    }

    public enum AllEntityTags {
        BLAZE_FREEZER_CAPTURABLE;

        public final TagKey<EntityType<?>> tag;

        AllEntityTags() {
            this(MOD);
        }

        AllEntityTags(NameSpace namespace) {
            this(namespace, null);
        }

        AllEntityTags(NameSpace namespace, @Nullable String pathOverride) {
            this.tag = TagKey.create(Registries.ENTITY_TYPE, namespace.id(this, pathOverride));
        }

        public boolean matches(EntityType<?> type) {
            return type.is(tag);
        }

        public boolean matches(Entity entity) {
            return matches(entity.getType());
        }
    }
}
