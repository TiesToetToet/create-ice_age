package com.tiestoettoet.create_ice_age.foundation.data;

import com.simibubi.create.foundation.block.CopperRegistries;
import com.tiestoettoet.create_ice_age.api.data.datamaps.BreezeFreezerFuel;
import com.tiestoettoet.create_ice_age.api.registry.CreateIceAgeDataMaps;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Oxidizable;
import net.neoforged.neoforge.registries.datamaps.builtin.Waxable;

import java.util.concurrent.CompletableFuture;

public class CreateIceAgeDatamapProvider extends DataMapProvider {
    public CreateIceAgeDatamapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
//        final Builder<Oxidizable, Block> oxidizables = builder(NeoForgeDataMaps.OXIDIZABLES);
//        CopperRegistries.getWeatheringView().forEach((now, after) -> add(oxidizables, now, new Oxidizable(after.value())));
//
//        final Builder<Waxable, Block> waxables = builder(NeoForgeDataMaps.WAXABLES);
//        CopperRegistries.getWaxableView().forEach((now, after) -> add(waxables, now, new Waxable(after.value())));

        var fuels = builder(CreateIceAgeDataMaps.REGULAR_FREEZE_FUELS);
        fuels.add(
                Items.SNOWBALL.builtInRegistryHolder(),
                new BreezeFreezerFuel(200),
                false
        );

        fuels.add(
                Items.ICE.builtInRegistryHolder(),
                new BreezeFreezerFuel(800),
                false
        );

        fuels.add(
                Items.PACKED_ICE.builtInRegistryHolder(),
                new BreezeFreezerFuel(1600),
                false
        );

        fuels.add(
                Items.BLUE_ICE.builtInRegistryHolder(),
                new BreezeFreezerFuel(3200),
                false
        );
    }

    public static <T> void add(Builder<T, Block> b, Holder<Block> now, T after) {
        b.add(now, after, false);
    }
}
