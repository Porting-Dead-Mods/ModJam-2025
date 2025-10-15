package com.portingdeadmods.modjam.client.screens.widgets;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.content.block.UpgradeBlockEntity;
import com.portingdeadmods.modjam.content.menus.PlanetSimulatorMenu;
import com.portingdeadmods.modjam.content.menus.UpgradeSlot;
import com.portingdeadmods.modjam.networking.UpgradeWidgetOpenClosePayload;
import com.portingdeadmods.modjam.networking.UpgradeWidgetSetSlotPositionsPayload;
import com.portingdeadmods.portingdeadlibs.api.client.screens.widgets.MenuWidgetContext;
import com.portingdeadmods.portingdeadlibs.api.client.screens.widgets.PanelWidget;
import com.portingdeadmods.portingdeadlibs.api.gui.menus.PDLAbstractContainerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

public class UpgradePanelWidget extends PanelWidget {
    public static final ResourceLocation WIDGET_SPRITE = Modjam.rl("widgets/widget_upgrade_right");
    public static final ResourceLocation WIDGET_OPEN_SPRITE = Modjam.rl("widgets/upgrade_widget_open");
    public static final int WIDGET_WIDTH = 32, WIDGET_HEIGHT = 32;
    public static final int WIDGET_OPEN_WIDTH = 32, WIDGET_OPEN_HEIGHT = 112;

    private UpgradeBlockEntity upgradeBlockEntity;

    public UpgradePanelWidget(int x, int y) {
        super(x, y, WIDGET_OPEN_WIDTH - 8, WIDGET_OPEN_HEIGHT - 8, WIDGET_WIDTH - 8, WIDGET_HEIGHT - 8);
        this.open = false;
    }

    @Override
    public void setContext(MenuWidgetContext context) {
        super.setContext(context);
        this.upgradeBlockEntity = (UpgradeBlockEntity) context.menu().blockEntity;
        PacketDistributor.sendToServer(new UpgradeWidgetOpenClosePayload(false));
        for (UpgradeSlot upgradeSlot : ((PlanetSimulatorMenu) this.context.menu()).getUpgradeSlots()) {
            upgradeSlot.setActive(false);
        }

        PacketDistributor.sendToServer(new UpgradeWidgetSetSlotPositionsPayload(27));
        PlanetSimulatorMenu menu = (PlanetSimulatorMenu) this.context.menu();
        menu.setUpgradeSlotPositions(27);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean isHovered = mouseX >= this.getX()
                && mouseY >= this.getY()
                && mouseX < this.getX() + this.getClosedWidth()
                && mouseY < this.getY() + (this.getClosedHeight() - 4);

        if (isHovered) {
            this.open = !this.open;

            PacketDistributor.sendToServer(new UpgradeWidgetOpenClosePayload(open));
            for (UpgradeSlot upgradeSlot : ((PlanetSimulatorMenu) this.context.menu()).getUpgradeSlots()) {
                upgradeSlot.setActive(this.open);
            }

            if (open) {
                this.setSize(WIDGET_OPEN_WIDTH, WIDGET_OPEN_HEIGHT);
            } else {
                this.setSize(WIDGET_WIDTH, WIDGET_HEIGHT);
            }
            this.context.onWidgetResizeFunc().accept(this);
            return super.mouseClicked(mouseX, mouseY, button);
        }
        return false;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {
        if (open) {
//            Font font = Minecraft.getInstance().font;
//
            guiGraphics.blitSprite(WIDGET_OPEN_SPRITE, getX(), getY(), WIDGET_OPEN_WIDTH, WIDGET_OPEN_HEIGHT);
//
//            guiGraphics.renderFakeItem(REDSTONE_STACK, getX() + 3, getY() + 8);
//            guiGraphics.drawString(font, Component.literal("Redstone").withStyle(ChatFormatting.WHITE), getX() + 20, getY() + 13, -1);
//
//            guiGraphics.drawString(font, Component.literal("Signal").withStyle(ChatFormatting.GRAY), getX() + 5, getY() + 54, -1);
//            RedstoneBlockEntity.RedstoneSignalType signalType = this.upgradeBlockEntity.getRedstoneSignalType();
//            if (signalType == null) signalType = RedstoneBlockEntity.RedstoneSignalType.IGNORED;
//            guiGraphics.drawString(font, Component.translatable("redstone_signal_type." + IndustrialReforged.MODID + "." + signalType.getSerializedName()).withStyle(ChatFormatting.WHITE), getX() + 5, getY() + 54 + font.lineHeight + 2, -1);
        } else {
            guiGraphics.blitSprite(WIDGET_SPRITE, getX(), getY(), WIDGET_WIDTH, WIDGET_HEIGHT);
        }
    }

    @Override
    public void onWidgetResized(PanelWidget resizedWidget) {
        super.onWidgetResized(resizedWidget);

        PlanetSimulatorMenu menu = ((PlanetSimulatorMenu) this.context.menu());
        if (resizedWidget.isOpen()) {
            PacketDistributor.sendToServer(new UpgradeWidgetSetSlotPositionsPayload(27 + resizedWidget.getOpenHeight()));
            menu.setUpgradeSlotPositions(27 + resizedWidget.getOpenHeight());
        } else {
            PacketDistributor.sendToServer(new UpgradeWidgetSetSlotPositionsPayload(27));
            menu.setUpgradeSlotPositions(27);
        }

    }

}