package com.portingdeadmods.spaceploitation.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Optional;

public record PlanetComponent(Optional<PlanetType> planetType, boolean activated, boolean isBlackHole) {
    public PlanetComponent {
        if (isBlackHole && planetType.isPresent() && planetType.get().projectionTexture().isPresent()) {
            throw new IllegalArgumentException("Planet card cannot be both black hole and have projection texture");
        }
    }
    public static final Codec<PlanetComponent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            PlanetType.CODEC.optionalFieldOf("planet_type").forGetter(PlanetComponent::planetType),
            Codec.BOOL.optionalFieldOf("activated", false).forGetter(PlanetComponent::activated),
            Codec.BOOL.optionalFieldOf("is_black_hole", false).forGetter(PlanetComponent::isBlackHole)
    ).apply(inst, PlanetComponent::new));
    
    public static final StreamCodec<? super RegistryFriendlyByteBuf, PlanetComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(PlanetType.STREAM_CODEC),
            PlanetComponent::planetType,
            ByteBufCodecs.BOOL,
            PlanetComponent::activated,
            ByteBufCodecs.BOOL,
            PlanetComponent::isBlackHole,
            PlanetComponent::new
    );
    
    public static final PlanetComponent EMPTY = new PlanetComponent(Optional.empty(), false, false);
}
