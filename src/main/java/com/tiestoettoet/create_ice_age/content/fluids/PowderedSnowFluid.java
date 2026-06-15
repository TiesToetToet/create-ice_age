package com.tiestoettoet.create_ice_age.content.fluids;

import com.simibubi.create.content.fluids.VirtualFluid;
import com.simibubi.create.content.fluids.potion.PotionFluid;
import com.tiestoettoet.create_ice_age.AllFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class PowderedSnowFluid extends VirtualFluid {

    public static PowderedSnowFluid createSource(Properties properties) {
        return new PowderedSnowFluid(properties, true);
    }

    public static PowderedSnowFluid createFlowing(Properties properties) {
        return new PowderedSnowFluid(properties, false);
    }

    protected PowderedSnowFluid(Properties properties, boolean source) {
        super(properties, source);
    }

    @Override
    public Item getBucket() {
        return Items.POWDER_SNOW_BUCKET;
    }

    @Override
    public Vec3 getFlow(BlockGetter blockReader, BlockPos pos, FluidState fluidState) {
        return Vec3.ZERO;
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluid, Direction direction) {
        return false;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState state) {
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean isSource(FluidState state) {
        return false;
    }

    @Override
    public int getAmount(FluidState state) {
        return 0;
    }

    @Override
    public VoxelShape getShape(FluidState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }
}
