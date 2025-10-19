package com.portingdeadmods.modjam.client.screens;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.client.screens.widgets.ModifiableFittingMultiLineTextWidget;
import com.portingdeadmods.modjam.client.screens.widgets.PlanetCardPanelWidget;
import com.portingdeadmods.modjam.client.screens.widgets.UpgradePanelWidget;
import com.portingdeadmods.modjam.content.menus.PlanetSimulatorMenu;
import com.portingdeadmods.portingdeadlibs.api.client.screens.PanelContainerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class PlanetSimulatorScreen extends PanelContainerScreen<PlanetSimulatorMenu> {
    public static final ResourceLocation BACKGROUND = Modjam.rl("textures/gui/planet_simulator.png");

    private ModifiableFittingMultiLineTextWidget lineTextWidget;

    public PlanetSimulatorScreen(PlanetSimulatorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 198;
        this.inventoryLabelY = 1000;
    }

    @Override
    protected void init() {
        super.init();

        UpgradePanelWidget upgradePanelWidget = new UpgradePanelWidget(this.leftPos + this.imageWidth, this.topPos + 4);
        this.addPanelWidget(upgradePanelWidget);
        this.addPanelWidget(new PlanetCardPanelWidget(this.leftPos + this.imageWidth, this.topPos + 4 + upgradePanelWidget.getHeight()));
        this.lineTextWidget = this.addRenderableWidget(new ModifiableFittingMultiLineTextWidget(this.leftPos + 10, this.topPos + 12, 126 - 12, 94, Component.empty(), Minecraft.getInstance().font));
        this.lineTextWidget.setBorderVisible(false);
        this.lineTextWidget.setY(this.lineTextWidget.getY() + 4);
    }

    @Override
    public @NotNull ResourceLocation getBackgroundTexture() {
        return BACKGROUND;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        this.lineTextWidget.setMessage(this.getInfo());
    }

    private Component getInfo() {
        return Component.literal(menu.blockEntity.getDisplayText());
    }
}
