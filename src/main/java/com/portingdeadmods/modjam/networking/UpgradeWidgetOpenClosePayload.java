package com.portingdeadmods.modjam.networking;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.content.menus.PlanetSimulatorMenu;
import com.portingdeadmods.modjam.content.menus.UpgradeSlot;
import com.portingdeadmods.portingdeadlibs.utils.codec.CodecUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpgradeWidgetOpenClosePayload(boolean open) implements CustomPacketPayload {
    public static final Type<UpgradeWidgetOpenClosePayload> TYPE = new Type<>(Modjam.rl("upgrade_widget_open_close"));
    public static final StreamCodec<ByteBuf, UpgradeWidgetOpenClosePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            UpgradeWidgetOpenClosePayload::open,
            UpgradeWidgetOpenClosePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().containerMenu instanceof PlanetSimulatorMenu machineMenu) {
                for (UpgradeSlot upgradeSlot : machineMenu.getUpgradeSlots()) {
                    upgradeSlot.setActive(this.open);
                }
            }
        }).exceptionally(err -> {
            Modjam.LOGGER.error("Failed to handle UpgradeWidgetOpenClose payload", err);
            return null;
        });
    }
}