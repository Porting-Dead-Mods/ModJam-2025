package com.portingdeadmods.modjam.registries;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.data.PlanetComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MJDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Modjam.MODID);

    public static final Supplier<DataComponentType<PlanetComponent>> PLANET = DATA_COMPONENTS.registerComponentType("planet", builder -> builder
            .persistent(PlanetComponent.CODEC)
            .networkSynchronized(PlanetComponent.STREAM_CODEC));
}
