package com.portingdeadmods.modjam.client.screens.widgets;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.content.menus.PlanetSimulatorMenu;
import com.portingdeadmods.modjam.networking.PlanetCardWidgetSetSlotPositionPayload;
import com.portingdeadmods.portingdeadlibs.api.client.screens.widgets.PanelWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

public class PlanetCardPanelWidget extends PanelWidget {
    public static final ResourceLocation WIDGET_SPRITE = Modjam.rl("widgets/widget_planet_card_right");
    public static final int WIDGET_WIDTH = 32, WIDGET_HEIGHT = 32;

    public PlanetCardPanelWidget(int x, int y) {
        super(x, y, WIDGET_WIDTH, WIDGET_HEIGHT, WIDGET_WIDTH, WIDGET_HEIGHT);
        PlanetSimulatorMenu menu = (PlanetSimulatorMenu) this.context.menu();
        menu.setPlanetCardSlotPosition(y);
        PacketDistributor.sendToServer(new PlanetCardWidgetSetSlotPositionPayload(y));
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float v) {
        guiGraphics.blitSprite(WIDGET_SPRITE, this.getX(), this.getY(), WIDGET_WIDTH, WIDGET_HEIGHT);
    }

    @Override
    public void onWidgetResized(PanelWidget resizedWidget) {
        PlanetSimulatorMenu menu = (PlanetSimulatorMenu) this.context.menu();
        int y;
        if (!resizedWidget.isOpen()) {
            y = 32;
        } else {
            y = 10 + resizedWidget.getOpenHeight();
        }
        menu.setPlanetCardSlotPosition(y);
        PacketDistributor.sendToServer(new PlanetCardWidgetSetSlotPositionPayload(y));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

}
