package com.tiestoettoet.create_ice_age.foundation.damageTypes;
import com.tiestoettoet.create_ice_age.AllDamageTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.Nullable;

public class CreateIceAgeDamageTypes {
    public static DamageSource frostbite(Level level) {
        return source(AllDamageTypes.FROSTBITE, level);
    }

    private static DamageSource source(ResourceKey<DamageType> key, LevelReader level) {
        Registry<DamageType> registry = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
        return new DamageSource(registry.getHolderOrThrow(key));
    }
}
