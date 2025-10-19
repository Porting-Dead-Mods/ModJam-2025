package com.portingdeadmods.spaceploitation;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = Spaceploitation.MODID)
public final class MJConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue COMPRESSOR_CAPACITY = BUILDER
            .comment("Compressor energy capacity")
            .defineInRange("compressor_capacity", 10000, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue COMPRESSOR_USAGE = BUILDER
            .comment("Compressor energy usage per tick")
            .defineInRange("compressor_usage", 20, 0, Integer.MAX_VALUE);

    static final ModConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) return;



    }
}
