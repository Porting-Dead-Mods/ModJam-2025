package com.portingdeadmods.modjam.registries;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.data.PlanetType;
import com.portingdeadmods.modjam.data.UpgradeType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

public final class MJRegistries {
    public static final ResourceKey<Registry<PlanetType>> PLANET_TYPE_KEY = ResourceKey.createRegistryKey(Modjam.rl("planet_type"));
    public static final ResourceKey<Registry<UpgradeType>> UPGRADE_TYPE_KEY = ResourceKey.createRegistryKey(Modjam.rl("upgrade_type"));

    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(PLANET_TYPE_KEY, PlanetType.CODEC, PlanetType.CODEC);
        event.dataPackRegistry(UPGRADE_TYPE_KEY, UpgradeType.CODEC, UpgradeType.CODEC);
    }
}
