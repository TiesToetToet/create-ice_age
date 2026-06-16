package com.tiestoettoet.create_ice_age.infrastructure.data;

import com.tiestoettoet.create_ice_age.AllDamageTypes;
import com.tiestoettoet.create_ice_age.CreateIceAge;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class GeneratedEntriesProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, AllDamageTypes::bootstrap);

    public GeneratedEntriesProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries, BUILDER, Set.of(CreateIceAge.MOD_ID));
    }

    @Override
    public String getName() {
        return "Create Ice Age's Generated Registry Entries";
    }
}
