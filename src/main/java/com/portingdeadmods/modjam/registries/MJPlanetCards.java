package com.portingdeadmods.modjam.registries;

import com.portingdeadmods.modjam.data.PlanetComponent;
import com.portingdeadmods.modjam.data.PlanetType;
import com.portingdeadmods.modjam.networking.MJNetworking;
import com.portingdeadmods.modjam.networking.SyncPlanetCardsPayload;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class MJPlanetCards {
    private static final List<ItemStack> PLANET_CARD_STACKS = new ArrayList<>();
    private static final List<PlanetType> PLANET_TYPES = new ArrayList<>();

    public static void registerPlanetCards(AddReloadListenerEvent event) {
        var registryLookup = event.getRegistryAccess().lookupOrThrow(MJRegistries.PLANET_TYPE_KEY);
        
        PLANET_CARD_STACKS.clear();
        PLANET_TYPES.clear();
        
        for (Holder.Reference<PlanetType> holder : registryLookup.listElements().toList()) {
            PlanetType planetType = holder.value();
            PLANET_TYPES.add(planetType);
            
            Item itemToUse = planetType.tint().isPresent() ? MJItems.TINTED_PLANET_CARD.get() : MJItems.PLANET_CARD.get();
            ItemStack stack = new ItemStack(itemToUse);
            stack.set(MJDataComponents.PLANET, new PlanetComponent(Optional.of(planetType), false, planetType.isBlackHole()));
            PLANET_CARD_STACKS.add(stack);
        }
    }

    public static void syncToPlayer(ServerPlayer player) {
        if (!PLANET_TYPES.isEmpty()) {
            MJNetworking.sendToPlayer(player, new SyncPlanetCardsPayload(PLANET_TYPES));
        }
    }

    public static void syncToAllPlayers() {
        if (!PLANET_TYPES.isEmpty()) {
            MJNetworking.sendToAllPlayers(new SyncPlanetCardsPayload(PLANET_TYPES));
        }
    }

    public static List<ItemStack> getPlanetCardStacks() {
        return PLANET_CARD_STACKS;
    }

    public static void clearStacks() {
        PLANET_CARD_STACKS.clear();
    }

    public static void addStack(ItemStack stack) {
        PLANET_CARD_STACKS.add(stack);
    }

    public static List<PlanetType> getAllPlanetTypes() {
        return PLANET_TYPES;
    }
}
