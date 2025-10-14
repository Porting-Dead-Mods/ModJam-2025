package com.portingdeadmods.modjam.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.Optional;

public record PlanetType(ResourceLocation texture, Optional<Integer> tint, ResourceKey<Level> dimension) {
    public static final Codec<PlanetType> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ResourceLocation.CODEC.fieldOf("texture").forGetter(PlanetType::texture),
            Codec.INT.optionalFieldOf("tint").forGetter(PlanetType::tint),
            ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(PlanetType::dimension)
    ).apply(inst, PlanetType::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlanetType> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            PlanetType::texture,
            ByteBufCodecs.optional(ByteBufCodecs.INT),
            PlanetType::tint,
            ResourceKey.streamCodec(Registries.DIMENSION),
            PlanetType::dimension,
            PlanetType::new
    );
}
