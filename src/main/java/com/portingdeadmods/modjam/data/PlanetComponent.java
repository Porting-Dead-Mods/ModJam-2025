package com.portingdeadmods.modjam.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.Optional;

public record PlanetComponent(Optional<ResourceKey<Level>> dimension) {
    public static final Codec<PlanetComponent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ResourceKey.codec(Registries.DIMENSION).optionalFieldOf("dimension").forGetter(PlanetComponent::dimension)
    ).apply(inst, PlanetComponent::new));
    public static final StreamCodec<? super RegistryFriendlyByteBuf, PlanetComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(ResourceKey.streamCodec(Registries.DIMENSION)),
            PlanetComponent::dimension,
            PlanetComponent::new
    );
    public static final PlanetComponent EMPTY = new PlanetComponent(Optional.empty());
}
