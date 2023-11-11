package com.miketies.create_ice_age.item;

import com.miketies.create_ice_age.CreateIceAge;
import com.miketies.create_ice_age.fluid.IAFluids;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.item.CombustibleItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.miketies.create_ice_age.CreateIceAge.ICE_AGE_REGISTRATE;

public class IAItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CreateIceAge.MOD_ID);

    public static final ItemEntry<CombustibleItem> ICE_CAKE = ICE_AGE_REGISTRATE.item("ice_cake", CombustibleItem::new)
            .tag(AllTags.AllItemTags.UPRIGHT_ON_BELT.tag)
            .onRegister(i -> i.setBurnTime(6400))
            .register();

    public static final RegistryObject<Item> LIQUID_ICE_BUCKET = ITEMS.register("liquid_ice_bucket",
            () -> new BucketItem(IAFluids.SOURCE_LIQUID_ICE, new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
