package com.tiestoettoet.create_ice_age.api.registry;

import com.tiestoettoet.create_ice_age.CreateIceAge;
import com.tiestoettoet.create_ice_age.api.data.datamaps.BreezeFreezerFuel;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

public class CreateIceAgeDataMaps {
    public static final DataMapType<Item, BreezeFreezerFuel> REGULAR_FREEZE_FUELS = DataMapType.builder(
            CreateIceAge.asResource("regular_freeze_fuels"), Registries.ITEM, BreezeFreezerFuel.CODEC).build();

    public static final DataMapType<Item, BreezeFreezerFuel> SUPER_FREEZE_FUELS = DataMapType.builder(
            CreateIceAge.asResource("super_freeze_fuels"), Registries.ITEM, BreezeFreezerFuel.CODEC).build();

    private CreateIceAgeDataMaps() {
        throw new AssertionError("This class should not be instantiated");
    }
}
