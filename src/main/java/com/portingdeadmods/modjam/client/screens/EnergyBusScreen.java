package com.portingdeadmods.modjam.client.screens;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.client.screens.widgets.CustomEnergyBarWidget;
import com.portingdeadmods.modjam.content.menus.EnergyBusMenu;
import com.portingdeadmods.modjam.utils.ScreenTextureBuilder;
import com.portingdeadmods.portingdeadlibs.api.capabilities.EnergyStorageWrapper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class EnergyBusScreen extends AbstractContainerScreen<EnergyBusMenu> {
    private static ResourceLocation BACKGROUND;

    public EnergyBusScreen(EnergyBusMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        if (BACKGROUND == null) {
            BACKGROUND = ScreenTextureBuilder.create(Modjam.MODID, "energy_bus")
                    .buildAndCache();
        }

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        EnergyStorageWrapper energyWrapper = new EnergyStorageWrapper() {
            @Override
            public int getEnergyStored() {
                return menu.getEnergyStored();
            }

            @Override
            public int getEnergyCapacity() {
                return menu.getEnergyCapacity();
            }
        };
        addRenderableOnly(new CustomEnergyBarWidget(x + 80, y + 20, energyWrapper, "FE", true));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(BACKGROUND, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
