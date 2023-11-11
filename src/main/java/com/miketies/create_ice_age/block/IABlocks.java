package com.miketies.create_ice_age.block;

import com.miketies.create_ice_age.CreateIceAge;
import com.miketies.create_ice_age.fluid.IAFluids;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class IABlocks {
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, CreateIceAge.MOD_ID);

    public static final RegistryObject<LiquidBlock> LIQUID_ICE_BLOCK = BLOCKS.register("liquid_ice_block",
            () -> new LiquidIceBlock(IAFluids.SOURCE_LIQUID_ICE, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
