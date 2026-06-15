//package com.tiestoettoet.create_ice_age.client;
//
//import com.tiestoettoet.create_ice_age.AllFluids;
//import net.minecraft.resources.ResourceLocation;
//import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
//import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
//
//public class AllFluidClientExtensions {
//    public static void register(RegisterClientExtensionsEvent event) {
//        event.registerFluidType(new IClientFluidTypeExtensions() {
//
//            @Override
//            public ResourceLocation getStillTexture() {
//                return ResourceLocation.fromNamespaceAndPath("minecraft", "block/powder_snow");
//            }
//
//            @Override
//            public ResourceLocation getFlowingTexture() {
//                return ResourceLocation.fromNamespaceAndPath("minecraft", "block/powder_snow");
//            }
//
//        }, AllFluids.POWDER_SNOW_TYPE.get());
//    }
//}
