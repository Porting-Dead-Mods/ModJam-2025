package com.portingdeadmods.spaceploitation.client;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

//This doesn't do anything right now and I don't know how to properly implement it!!!
@OnlyIn(Dist.CLIENT)
public class PlanetColours implements ResourceManagerReloadListener {
    private static final Logger LOGGER = LogUtils.getLogger();
    private Map<ResourceKey<Level>, Integer> colourMap;

    private static Map<ResourceKey<Level>, Integer> extractColours(Stream<PackResources> packResources) {
        Map<ResourceKey<Level>, Integer> map = Maps.newHashMap();
        packResources.forEach((resources) -> {
            try {
                PlanetMetadataSelection planetMetadataSelection = resources.getMetadataSection(PlanetMetadataSelection.TYPE);
                if (planetMetadataSelection != null) {
                    Map<ResourceKey<Level>, Integer> colourMap = planetMetadataSelection.colourMap();
                    Objects.requireNonNull(map);
                    colourMap.forEach(map::putIfAbsent);
                }
            } catch (RuntimeException | IOException runtimeexception) {
                LOGGER.warn("Unable to parse planet colour metadata section of resourcepack: {}", resources.packId(), runtimeexception);
            }
        });
        return ImmutableMap.copyOf(map);
    }
    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        this.colourMap = extractColours(resourceManager.listPacks());
    }

    public int getColour(ResourceKey<Level> level) {
        return colourMap.getOrDefault(level, 0x6abe30);
    }

    record PlanetMetadataSelection(HashMap<ResourceKey<Level>, Integer> colourMap) {
        public static final Codec<PlanetMetadataSelection> CODEC;
        public static final MetadataSectionType<PlanetMetadataSelection> TYPE;

        public static final Codec<ResourceKey<Level>> PLANET_CODEC = ResourceLocation.CODEC.comapFlatMap(
                (location) -> DataResult.success(ResourceKey.create(Registries.DIMENSION, location)), ResourceKey::location);

        static {
            CODEC = RecordCodecBuilder.create(inst -> inst.group(
                    Codec.unboundedMap(PLANET_CODEC, Codec.INT).xmap(HashMap::new, HashMap::new).fieldOf("colours").forGetter(PlanetMetadataSelection::colourMap)
            ).apply(inst, PlanetMetadataSelection::new));
            TYPE = MetadataSectionType.fromCodec("planet_colours", CODEC);
        }
    }
}
