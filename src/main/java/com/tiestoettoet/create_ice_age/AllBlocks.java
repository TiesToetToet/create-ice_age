package com.tiestoettoet.create_ice_age;

import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.breeze_freezer.BreezeFreezerBlock;
import com.tiestoettoet.create_ice_age.content.processing.super_freezer.breeze_freezer.BreezeFreezerBlockItem;
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

    public static final BlockEntry<BreezeFreezerBlock> BREEZE_FREEZER = REGISTRATE
            .block("breeze_freezer", BreezeFreezerBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY))
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .loot((lt, block) -> lt.add(block, BreezeFreezerBlock.buildLootTable()))
            .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
            .item(BreezeFreezerBlockItem::withBreeze)
            .model(AssetLookup.customBlockItemModel("breeze_freezer", "block_with_breeze"))
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
