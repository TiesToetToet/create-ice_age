package com.miketies.create_ice_age.item;

import com.miketies.create_ice_age.CreateIceAge;
import com.miketies.create_ice_age.blaze_freezer.BlazeFreezerBlockItem;
import com.miketies.create_ice_age.fluid.IAFluids;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;

import static com.miketies.create_ice_age.CreateIceAge.ICE_AGE_REGISTRATE;

public class IAItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CreateIceAge.MOD_ID);

    public static final ItemEntry<FreezerItem> ICE_CAKE =
            ICE_AGE_REGISTRATE.item("ice_cake", FreezerItem::new)
                .tag(AllTags.AllItemTags.UPRIGHT_ON_BELT.tag)
                .onRegister(i -> i.setFreezeTime(6400))
                .register();
    public static final ItemEntry<Item> ICE_CAKE_BASE =
            ICE_AGE_REGISTRATE.item("ice_cake_base", Item::new)
                    .tag(AllTags.AllItemTags.UPRIGHT_ON_BELT.tag)
                    .register();

    public static final ItemEntry<BlazeFreezerBlockItem> BLAZE_FREEZER =
            ICE_AGE_REGISTRATE.item("blaze_freezer", BlazeFreezerBlockItem::new)
//                .model((c, p) -> p.blockItem(() -> c.getEntry().getBlock()))
                .model(AssetLookup.customBlockItemModel("blaze_freezer"))
                .register();

    public static final RegistryObject<Item> LIQUID_ICE_BUCKET = ITEMS.register("liquid_ice_bucket",
            () -> new BucketItem(IAFluids.SOURCE_LIQUID_ICE, new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
