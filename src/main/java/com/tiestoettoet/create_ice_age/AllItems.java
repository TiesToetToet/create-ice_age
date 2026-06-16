package com.tiestoettoet.create_ice_age;


import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tiestoettoet.create_ice_age.api.data.datamaps.BreezeFreezerFuel;
import com.tiestoettoet.create_ice_age.api.registry.CreateIceAgeDataMaps;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.breeze_freezer.BreezeFreezerBlockItem;
//import com.tiestoettoet.create_ice_age.content.processing.super_freezer.lid.BasinFreezerLidBlockItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;


public class AllItems {
    private static final CreateRegistrate REGISTRATE = CreateIceAge.registrate();

    static {
        REGISTRATE.setCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB);
    }

    public static final ItemEntry<Item> ICE_CAKE =
            REGISTRATE.item("ice_cake", Item::new)
                    .tag(AllTags.AllItemTags.UPRIGHT_ON_BELT.tag)
                    .dataMap(CreateIceAgeDataMaps.SUPER_FREEZE_FUELS, new BreezeFreezerFuel(3200))
                    .register();
    public static final ItemEntry<Item> ICE_CAKE_BASE =
            REGISTRATE.item("ice_cake_base", Item::new)
                    .tag(AllTags.AllItemTags.UPRIGHT_ON_BELT.tag)
                    .register();

//    public static final ItemEntry<BreezeFreezerBlockItem> BREEZE_FREEZER =
//            REGISTRATE.item("breeze_freezer", BreezeFreezerBlockItem::new)
//                    .model(AssetLookup.customBlockItemModel("breeze_freezer"))
//                    .register();
//
//    public static final ItemEntry<BasinFreezerLidBlockItem> BREEZE_FREEZER_LID =
//            REGISTRATE.item("basin_freezer_lid", BasinFreezerLidBlockItem::new)
//                    .model(AssetLookup.customBlockItemModel("basin_freezer_lid"))
//                    .register();

    public static void register() {
    }
}
