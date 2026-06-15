package com.tiestoettoet.create_ice_age;

import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.blaze_freezer.BlazeFreezerBlock;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.blaze_freezer.BlazeFreezerBlockItem;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.material.MapColor;

import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

@SuppressWarnings("removal")
public class AllBlocks {
    private static final CreateRegistrate REGISTRATE = CreateIceAge.registrate();

    static {
        REGISTRATE.setCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB);
    }

    public static final BlockEntry<BlazeFreezerBlock> BLAZE_FREEZER = REGISTRATE
            .block("blaze_freezer", BlazeFreezerBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY))
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .loot((lt, block) -> lt.add(block, BlazeFreezerBlock.buildLootTable()))
            .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
            .item(BlazeFreezerBlockItem::withBlaze)
            .model(AssetLookup.customBlockItemModel("blaze_freezer", "block_with_blaze"))
            .build()
            .register();

//    public static final BlockEntry<BasinFreezerLidBlock> BASIN_FREEZER_LID = REGISTRATE
//            .block("basin_freezer_lid", BasinFreezerLidBlock::new)
//            .initialProperties(SharedProperties::softMetal)
//            .blockstate(BlockStateGen.horizontalBlockProvider(true))
//            .item()
//            .model(AssetLookup.customBlockItemModel("basin_freezer_lid", "block"))
//            .build()
//            .register();

    public static void register() {
    }
}
