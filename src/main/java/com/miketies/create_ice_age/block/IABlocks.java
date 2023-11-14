package com.miketies.create_ice_age.block;

import com.miketies.create_ice_age.CreateIceAge;
import com.miketies.create_ice_age.super_freezer.blaze_freezer.BlazeFreezerBlock;
import com.miketies.create_ice_age.fluid.IAFluids;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.miketies.create_ice_age.CreateIceAge.ICE_AGE_REGISTRATE;

public class IABlocks {
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, CreateIceAge.MOD_ID);

    public static final RegistryObject<LiquidBlock> LIQUID_ICE_BLOCK = BLOCKS.register("liquid_ice_block",
            () -> new LiquidIceBlock(IAFluids.SOURCE_LIQUID_ICE, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final BlockEntry<BlazeFreezerBlock> BLAZE_FREEZER = ICE_AGE_REGISTRATE
            .block("blaze_freezer", BlazeFreezerBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .blockstate((ctx, pov) -> pov.simpleBlock(ctx.get(), AssetLookup.standardModel(ctx, pov)))
            .register();

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    public static void register() {}
}
