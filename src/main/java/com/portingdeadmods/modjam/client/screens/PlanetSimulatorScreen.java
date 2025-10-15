package com.portingdeadmods.modjam.client.screens;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.client.screens.widgets.PlanetCardPanelWidget;
import com.portingdeadmods.modjam.client.screens.widgets.UpgradePanelWidget;
import com.portingdeadmods.modjam.content.menus.PlanetSimulatorMenu;
import com.portingdeadmods.portingdeadlibs.api.client.screens.PanelContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class PlanetSimulatorScreen extends PanelContainerScreen<PlanetSimulatorMenu> {
    public static final ResourceLocation BACKGROUND = Modjam.rl("textures/gui/planet_simulator.png");

    public PlanetSimulatorScreen(PlanetSimulatorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 198;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();

        UpgradePanelWidget upgradePanelWidget = new UpgradePanelWidget(this.leftPos + this.imageWidth, this.topPos + 4);
        this.addPanelWidget(upgradePanelWidget);
        this.addPanelWidget(new PlanetCardPanelWidget(this.leftPos + this.imageWidth, this.topPos + 4 + upgradePanelWidget.getHeight()));

    }

    @Override
    public @NotNull ResourceLocation getBackgroundTexture() {
        return BACKGROUND;
    }

}
