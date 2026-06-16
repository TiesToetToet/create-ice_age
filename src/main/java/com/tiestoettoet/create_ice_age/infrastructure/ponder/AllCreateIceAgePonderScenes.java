package com.tiestoettoet.create_ice_age.infrastructure.ponder;

import com.simibubi.create.AllItems;
import com.tiestoettoet.create_ice_age.AllBlocks;
import com.tiestoettoet.create_ice_age.infrastructure.ponder.scenes.ProcessingScenes;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class AllCreateIceAgePonderScenes {
    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);
        HELPER.addStoryBoard(AllItems.EMPTY_BLAZE_BURNER, "empty_blaze_burner", ProcessingScenes::emptyBlazeBurner);
        HELPER.addStoryBoard(AllBlocks.BREEZE_FREEZER, "breeze_freezer", ProcessingScenes::breezeFreezer);
    }
}
