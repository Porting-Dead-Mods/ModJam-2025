package com.portingdeadmods.modjam.networking;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.data.PlanetType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record SyncPlanetCardsPayload(List<PlanetType> planetTypes) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncPlanetCardsPayload> TYPE = 
            new CustomPacketPayload.Type<>(Modjam.rl("sync_planet_cards"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncPlanetCardsPayload> STREAM_CODEC = StreamCodec.composite(
            PlanetType.STREAM_CODEC.apply(ByteBufCodecs.list()),
            SyncPlanetCardsPayload::planetTypes,
            SyncPlanetCardsPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
