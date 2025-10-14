package com.portingdeadmods.modjam.client.screens;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.client.screens.widgets.UpgradePanelWidget;
import com.portingdeadmods.modjam.content.menus.PlanetSimulatorMenu;
import com.portingdeadmods.modjam.networking.UpgradeWidgetSetSlotPositionsPayload;
import com.portingdeadmods.portingdeadlibs.api.client.screens.PDLAbstractContainerScreen;
import com.portingdeadmods.portingdeadlibs.api.client.screens.PanelContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class PlanetSimulatorScreen extends PanelContainerScreen<PlanetSimulatorMenu> {

    public static final ResourceLocation BACKGROUND = Modjam.rl("textures/gui/planet_simulator.png");

    public PlanetSimulatorScreen(PlanetSimulatorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

    }

    @Override
    protected void init() {
        super.init();

        addPanelWidget(new UpgradePanelWidget(this.leftPos + this.imageWidth, this.topPos + 2 + 24 + 2));

        PacketDistributor.sendToServer(new UpgradeWidgetSetSlotPositionsPayload(51));
        this.menu.setUpgradeSlotPositions(51);
    }

    @Override
    public @NotNull ResourceLocation getBackgroundTexture() {
        return BACKGROUND;
    }
}
