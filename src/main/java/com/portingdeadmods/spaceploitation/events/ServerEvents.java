package com.portingdeadmods.spaceploitation.events;

import com.mojang.brigadier.Command;
import com.portingdeadmods.spaceploitation.Spaceploitation;
import com.portingdeadmods.spaceploitation.registries.MJDataAttachments;
import com.portingdeadmods.spaceploitation.registries.MJItems;
import com.portingdeadmods.spaceploitation.registries.MJPlanetCards;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = Spaceploitation.MODID)
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
            
            if (!serverPlayer.getData(MJDataAttachments.HAS_RECEIVED_GUIDE)) {
                ItemStack guideBook = MJItems.GUIDE.get().getDefaultInstance();
                if (!serverPlayer.getInventory().add(guideBook)) {
                    serverPlayer.drop(guideBook, false);
                }
                serverPlayer.setData(MJDataAttachments.HAS_RECEIVED_GUIDE, true);
            }
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

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
            Commands.literal("itemdata")
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    ItemStack heldItem = player.getMainHandItem();

                    if (heldItem.isEmpty()) {
                        player.sendSystemMessage(Component.literal("No item in hand").withStyle(ChatFormatting.RED));
                        return 0;
                    }

                    player.sendSystemMessage(Component.literal("=== Item Data Components ===").withStyle(ChatFormatting.GOLD));
                    Spaceploitation.LOGGER.info("=== Item Data Components for {} ===", heldItem.getItem().toString());

                    player.sendSystemMessage(Component.literal("Item: " + heldItem.getDisplayName().getString()).withStyle(ChatFormatting.YELLOW));
                    Spaceploitation.LOGGER.info("Item: {}", heldItem.getDisplayName().getString());

                    for (TypedDataComponent<?> component : heldItem.getComponents()) {
                        String componentInfo = component.type().toString() + " = " + component.value().toString();
                        player.sendSystemMessage(Component.literal("  " + componentInfo).withStyle(ChatFormatting.AQUA));
                        Spaceploitation.LOGGER.info("  {}", componentInfo);
                    }

                    player.sendSystemMessage(Component.literal("=== End Data Components ===").withStyle(ChatFormatting.GOLD));
                    Spaceploitation.LOGGER.info("=== End Data Components ===");

                    return Command.SINGLE_SUCCESS;
                })
        );
    }
}
