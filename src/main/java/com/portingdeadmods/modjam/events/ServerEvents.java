package com.portingdeadmods.modjam.events;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.registries.MJPlanetCards;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = Modjam.MODID)
public class ServerEvents {

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {

    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {

    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {

    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {

    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            MJPlanetCards.syncToPlayer(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {

    }

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        MJPlanetCards.registerPlanetCards(event);
        if (EffectiveSide.get() == LogicalSide.SERVER) {
            MJPlanetCards.syncToAllPlayers();
        }
    }
}
