package com.portingdeadmods.spaceploitation.registries;

import com.portingdeadmods.spaceploitation.Spaceploitation;
import com.portingdeadmods.spaceploitation.data.PlanetComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MJDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Spaceploitation.MODID);

    public static final Supplier<DataComponentType<PlanetComponent>> PLANET = DATA_COMPONENTS.registerComponentType("planet", builder -> builder
            .persistent(PlanetComponent.CODEC)
            .networkSynchronized(PlanetComponent.STREAM_CODEC));
}
