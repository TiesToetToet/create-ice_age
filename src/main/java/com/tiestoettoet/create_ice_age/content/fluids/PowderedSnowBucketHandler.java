package com.tiestoettoet.create_ice_age.content.fluids;

import com.tiestoettoet.create_ice_age.AllFluids;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

public class PowderedSnowBucketHandler {


    public static void register(RegisterCapabilitiesEvent event) {
        event.registerItem(
                Capabilities.FluidHandler.ITEM,
                (stack, context) -> new PowderedSnowBucketFluidHandler(stack),
                Items.POWDER_SNOW_BUCKET
        );
    }

    public static class PowderedSnowBucketFluidHandler implements IFluidHandlerItem {

        private final ItemStack container;

        public PowderedSnowBucketFluidHandler(ItemStack container) {
            this.container = container.copy();
            this.container.setCount(1);
        }

        @Override
        public @NotNull ItemStack getContainer() {
            return new ItemStack(Items.BUCKET);
        }

        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public @NotNull FluidStack getFluidInTank(int tank) {
            if (tank != 0) return FluidStack.EMPTY;
            if (container.is(Items.POWDER_SNOW_BUCKET)) {
                return new FluidStack(AllFluids.POWDER_SNOW.get(), FluidType.BUCKET_VOLUME);
            }
            return FluidStack.EMPTY;
        }

        @Override
        public int getTankCapacity(int tank) {
            return tank == 0 ? FluidType.BUCKET_VOLUME : 0;
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return tank == 0 && stack.is(AllFluids.POWDER_SNOW.get());
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            return 0;
        }

        @Override
        public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
            if (!resource.is(AllFluids.POWDER_SNOW.get())) {
                return FluidStack.EMPTY;
            }
            return drain(resource.getAmount(), action);
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            if (!container.is(Items.POWDER_SNOW_BUCKET)) {
                return FluidStack.EMPTY;
            }

            if (maxDrain < FluidType.BUCKET_VOLUME) {
                return FluidStack.EMPTY;
            }

            return new FluidStack(AllFluids.POWDER_SNOW.get(), FluidType.BUCKET_VOLUME);
        }
    }
}