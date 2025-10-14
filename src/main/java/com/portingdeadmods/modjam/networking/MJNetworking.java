package com.portingdeadmods.modjam.networking;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.data.PlanetComponent;
import com.portingdeadmods.modjam.registries.MJDataComponents;
import com.portingdeadmods.modjam.registries.MJItems;
import com.portingdeadmods.modjam.registries.MJPlanetCards;
import com.portingdeadmods.portingdeadlibs.PortingDeadLibsClient;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.Optional;

public class MJNetworking {

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(Modjam.MODID);
        
        registrar.playToClient(
                SyncPlanetCardsPayload.TYPE,
                SyncPlanetCardsPayload.STREAM_CODEC,
                MJNetworking::handleSyncPlanetCards
        );
    }

    private static void handleSyncPlanetCards(SyncPlanetCardsPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            MJPlanetCards.clearStacks();
            
            payload.planetTypes().forEach(planetType -> {
                ItemStack stack = new ItemStack(MJItems.PLANET_CARD.get());
                stack.set(MJDataComponents.PLANET, new PlanetComponent(Optional.of(planetType), false));
                MJPlanetCards.addStack(stack);
            });
            
            if (Minecraft.getInstance().player != null) {
                PortingDeadLibsClient.markTabsDirty();
            }
        });
    }

    public static void sendToServer(CustomPacketPayload packet) {
        PacketDistributor.sendToServer(packet);
    }

    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload packet) {
        PacketDistributor.sendToPlayer(player, packet);
    }

    public static void sendToAllPlayers(CustomPacketPayload packet) {
        PacketDistributor.sendToAllPlayers(packet);
    }
}
