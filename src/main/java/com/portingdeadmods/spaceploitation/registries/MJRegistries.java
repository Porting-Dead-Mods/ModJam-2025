package com.portingdeadmods.spaceploitation.registries;

import com.portingdeadmods.spaceploitation.Spaceploitation;
import com.portingdeadmods.spaceploitation.data.PlanetType;
import com.portingdeadmods.spaceploitation.data.UpgradeType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

public final class MJRegistries {
    public static final ResourceKey<Registry<PlanetType>> PLANET_TYPE_KEY = ResourceKey.createRegistryKey(Spaceploitation.rl("planet_type"));
    public static final ResourceKey<Registry<UpgradeType>> UPGRADE_TYPE_KEY = ResourceKey.createRegistryKey(Spaceploitation.rl("upgrade_type"));

    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(PLANET_TYPE_KEY, PlanetType.CODEC, PlanetType.CODEC);
        event.dataPackRegistry(UPGRADE_TYPE_KEY, UpgradeType.CODEC, UpgradeType.CODEC);
    }
}
