package com.portingdeadmods.modjam.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Optional;

public record PlanetComponent(Optional<PlanetType> planetType, boolean activated) {
    public static final Codec<PlanetComponent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            PlanetType.CODEC.optionalFieldOf("planet_type").forGetter(PlanetComponent::planetType),
            Codec.BOOL.optionalFieldOf("activated", false).forGetter(PlanetComponent::activated)
    ).apply(inst, PlanetComponent::new));
    
    public static final StreamCodec<? super RegistryFriendlyByteBuf, PlanetComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(PlanetType.STREAM_CODEC),
            PlanetComponent::planetType,
            ByteBufCodecs.BOOL,
            PlanetComponent::activated,
            PlanetComponent::new
    );
    
    public static final PlanetComponent EMPTY = new PlanetComponent(Optional.empty(), false);
}
