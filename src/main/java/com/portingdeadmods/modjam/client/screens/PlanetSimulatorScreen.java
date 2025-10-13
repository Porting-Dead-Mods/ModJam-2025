package com.portingdeadmods.modjam.client.screens;

import com.portingdeadmods.modjam.content.menus.PlanetSimulatorMenu;
import com.portingdeadmods.portingdeadlibs.api.client.screens.PDLAbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class PlanetSimulatorScreen extends PDLAbstractContainerScreen<PlanetSimulatorMenu> {
    public PlanetSimulatorScreen(PlanetSimulatorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public @NotNull ResourceLocation getBackgroundTexture() {
        return ResourceLocation.parse("");
    }
}
