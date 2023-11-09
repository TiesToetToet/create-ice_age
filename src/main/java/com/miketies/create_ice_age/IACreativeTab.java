package com.miketies.create_ice_age;

import com.miketies.create_ice_age.IAItems;
import com.miketies.create_ice_age.CreateIceAge;
import com.tterrag.registrate.util.entry.RegistryEntry;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class IACreativeTab {
    public static final DeferredRegister<CreativeModeTab> TAB_REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateIceAge.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ICE_AGE_CREATIVE_TAB =
            TAB_REGISTER.register("ice_age_creative_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.create_ice_age.ice_age_creative_tab"))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .icon(IAItems.ICE_CAKE::asStack)
                    .displayItems(new RegistrateDisplayItemsGenerator())
                    .build());

    public static void register(IEventBus eventBus) {
        TAB_REGISTER.register(eventBus);
    }

    public static class RegistrateDisplayItemsGenerator implements CreativeModeTab.DisplayItemsGenerator {

        private List<Item> collectBlocks(RegistryObject<CreativeModeTab> tab, Predicate<Item> exclusionPredicate) {
            List<Item> items = new ReferenceArrayList<>();
            for (RegistryEntry<Block> entry : CreateIceAge.ICE_AGE_REGISTRATE.getAll(Registries.BLOCK)) {
                if (!CreateIceAge.ICE_AGE_REGISTRATE.isInCreativeTab(entry, tab))
                    continue;
                Item item = entry.get()
                        .asItem();
                if (item == Items.AIR)
                    continue;
                if (!exclusionPredicate.test(item))
                    items.add(item);
            }
            items = new ReferenceArrayList<>(new ReferenceLinkedOpenHashSet<>(items));
            return items;
        }

        private List<Item> collectItems(RegistryObject<CreativeModeTab> tab, Predicate<Item> exclusionPredicate) {
            List<Item> items = new ReferenceArrayList<>();


            for (RegistryEntry<Item> entry : CreateIceAge.ICE_AGE_REGISTRATE.getAll(Registries.ITEM)) {
                if (!CreateIceAge.ICE_AGE_REGISTRATE.isInCreativeTab(entry, tab))
                    continue;
                Item item = entry.get();
                if (item instanceof BlockItem)
                    continue;
                if (!exclusionPredicate.test(item))
                    items.add(item);
            }
            return items;
        }

        private static void outputAll(CreativeModeTab.Output output, List<Item> items) {
            for (Item item : items) {
                output.accept(item);
            }
        }

//        List<Item> exclude = List.of(CAItems.CAKE_BASE.get(), CAItems.CAKE_BASE_BAKED.get());

        @Override
        public void accept(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output) {
            List<Item> items = new LinkedList<>();
            items.addAll(collectItems(ICE_AGE_CREATIVE_TAB, item -> false));

            outputAll(output, items);
        }
    }
}
