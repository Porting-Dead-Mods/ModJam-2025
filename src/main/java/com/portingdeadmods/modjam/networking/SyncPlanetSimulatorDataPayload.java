package com.portingdeadmods.modjam.networking;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.content.blockentity.PlanetSimulatorBlockEntity;
import com.portingdeadmods.modjam.content.recipe.PlanetSimulatorRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record SyncPlanetSimulatorDataPayload(
        BlockPos pos,
        String displayText,
        List<PlanetSimulatorRecipe.WeightedOutput> outputs
) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncPlanetSimulatorDataPayload> TYPE = 
            new CustomPacketPayload.Type<>(Modjam.rl("sync_planet_simulator_data"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncPlanetSimulatorDataPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            SyncPlanetSimulatorDataPayload::pos,
            ByteBufCodecs.STRING_UTF8,
            SyncPlanetSimulatorDataPayload::displayText,
            PlanetSimulatorRecipe.WeightedOutput.STREAM_CODEC.apply(ByteBufCodecs.list()),
            SyncPlanetSimulatorDataPayload::outputs,
            SyncPlanetSimulatorDataPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncPlanetSimulatorDataPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                BlockEntity be = Minecraft.getInstance().level.getBlockEntity(payload.pos);
                if (be instanceof PlanetSimulatorBlockEntity planetSimulator) {
                    planetSimulator.setClientDisplayText(payload.displayText);
                    planetSimulator.setClientOutputs(payload.outputs);
                }
            }
        });
    }
}
