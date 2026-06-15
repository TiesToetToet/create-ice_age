package com.tiestoettoet.create_ice_age.content.fluids;

import com.tiestoettoet.create_ice_age.AllFluids;
import com.tiestoettoet.create_ice_age.foundation.damageTypes.CreateIceAgeDamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber
public class LiquidIceEffects {

    private static final int MAX_FREEZE = 140;
    private static final int DAMAGE_INTERVAL = 40;
    private static final Map<UUID, Integer> FREEZE_PROGRESS = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof Player player))
            return;

        if (player.level().isClientSide)
            return;
        UUID id = player.getUUID();

        int frozen = FREEZE_PROGRESS.getOrDefault(id, 0);

//        int frozen = player.getTicksFrozen();
//        System.out.println("Player " + player.getName().getString() + " is frozen for " + frozen + " ticks.");

        boolean inLiquidIce =
                player.isInFluidType(AllFluids.LIQUID_ICE.get().getFluidType());

        boolean protectedByLeather =
                player.getInventory().armor.stream()
                        .anyMatch(stack -> stack.getItem() instanceof net.minecraft.world.item.ArmorItem armor
                                && armor.getMaterial().value().equipSound()
                                != null // replace with proper leather check
                        );

        if (inLiquidIce && !protectedByLeather) {

            frozen = Math.min(MAX_FREEZE, frozen + 1);
            player.setTicksFrozen(frozen);
            FREEZE_PROGRESS.put(id, frozen);

            if (frozen >= MAX_FREEZE && player.tickCount % DAMAGE_INTERVAL == 0) {

                player.hurt(
                        CreateIceAgeDamageTypes.frostbite(player.level()),
                        1.0F // 1 heart = 2 HP
                );
            }

        } else {
            frozen = Math.max(0, frozen - 2);
            player.setTicksFrozen(frozen);
            FREEZE_PROGRESS.put(id, frozen);
        }
    }
}