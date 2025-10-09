package com.portingdeadmods.modjam.events;

import com.portingdeadmods.modjam.Modjam;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
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

    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {

    }
}
