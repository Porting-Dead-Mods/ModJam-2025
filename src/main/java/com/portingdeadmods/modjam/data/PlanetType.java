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
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Optional;

public record PlanetType(
        ResourceLocation texture, 
        Optional<ResourceLocation> projectionTexture, 
        Optional<Integer> tint, 
        Optional<ResourceKey<Level>> dimension,
        Optional<ResourceKey<Biome>> biome,
        Optional<ResourceKey<Structure>> structure,
        boolean isBlackHole) {
    
    public static final Codec<PlanetType> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ResourceLocation.CODEC.fieldOf("texture").forGetter(PlanetType::texture),
            ResourceLocation.CODEC.optionalFieldOf("projection_texture").forGetter(PlanetType::projectionTexture),
            Codec.INT.optionalFieldOf("tint").forGetter(PlanetType::tint),
            ResourceKey.codec(Registries.DIMENSION).optionalFieldOf("dimension").forGetter(PlanetType::dimension),
            ResourceKey.codec(Registries.BIOME).optionalFieldOf("biome").forGetter(PlanetType::biome),
            ResourceKey.codec(Registries.STRUCTURE).optionalFieldOf("structure").forGetter(PlanetType::structure),
            Codec.BOOL.optionalFieldOf("is_black_hole", false).forGetter(PlanetType::isBlackHole)
    ).apply(inst, PlanetType::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlanetType> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public PlanetType decode(RegistryFriendlyByteBuf buf) {
            ResourceLocation texture = ResourceLocation.STREAM_CODEC.decode(buf);
            Optional<ResourceLocation> projectionTexture = ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC).decode(buf);
            Optional<Integer> tint = ByteBufCodecs.optional(ByteBufCodecs.INT).decode(buf);
            Optional<ResourceKey<Level>> dimension = ByteBufCodecs.optional(ResourceKey.streamCodec(Registries.DIMENSION)).decode(buf);
            Optional<ResourceKey<Biome>> biome = ByteBufCodecs.optional(ResourceKey.streamCodec(Registries.BIOME)).decode(buf);
            Optional<ResourceKey<Structure>> structure = ByteBufCodecs.optional(ResourceKey.streamCodec(Registries.STRUCTURE)).decode(buf);
            boolean isBlackHole = ByteBufCodecs.BOOL.decode(buf);
            return new PlanetType(texture, projectionTexture, tint, dimension, biome, structure, isBlackHole);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, PlanetType value) {
            ResourceLocation.STREAM_CODEC.encode(buf, value.texture());
            ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC).encode(buf, value.projectionTexture());
            ByteBufCodecs.optional(ByteBufCodecs.INT).encode(buf, value.tint());
            ByteBufCodecs.optional(ResourceKey.streamCodec(Registries.DIMENSION)).encode(buf, value.dimension());
            ByteBufCodecs.optional(ResourceKey.streamCodec(Registries.BIOME)).encode(buf, value.biome());
            ByteBufCodecs.optional(ResourceKey.streamCodec(Registries.STRUCTURE)).encode(buf, value.structure());
            ByteBufCodecs.BOOL.encode(buf, value.isBlackHole());
        }
    };
}
