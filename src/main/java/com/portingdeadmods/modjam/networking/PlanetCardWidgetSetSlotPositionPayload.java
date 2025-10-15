package com.portingdeadmods.modjam.networking;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.content.menus.PlanetSimulatorMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record PlanetCardWidgetSetSlotPositionPayload(int y) implements CustomPacketPayload {
    public static final Type<PlanetCardWidgetSetSlotPositionPayload> TYPE = new Type<>(Modjam.rl("planet_card_widget_set_slot_position"));
    public static final StreamCodec<ByteBuf, PlanetCardWidgetSetSlotPositionPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            PlanetCardWidgetSetSlotPositionPayload::y,
            PlanetCardWidgetSetSlotPositionPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().containerMenu instanceof PlanetSimulatorMenu machineMenu) {
                machineMenu.setPlanetCardSlotPosition(this.y);
            }
        }).exceptionally(err -> {
            Modjam.LOGGER.error("Failed to handle UpgradeWidgetSetSlotPositions payload", err);
            return null;
        });
    }
}