package com.portingdeadmods.spaceploitation.client.screens;

import com.portingdeadmods.spaceploitation.Spaceploitation;
import com.portingdeadmods.spaceploitation.content.menus.FluidBusMenu;
import com.portingdeadmods.spaceploitation.utils.ScreenTextureBuilder;
import com.portingdeadmods.portingdeadlibs.client.screens.widgets.FluidTankWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class FluidBusScreen extends AbstractContainerScreen<FluidBusMenu> {
    private static ResourceLocation BACKGROUND;

    public FluidBusScreen(FluidBusMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        if (BACKGROUND == null) {
            BACKGROUND = ScreenTextureBuilder.create(Spaceploitation.MODID, "fluid_bus")
                    .buildAndCache();
        }

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        IFluidHandler fluidHandler = new IFluidHandler() {
            @Override
            public int getTanks() {
                return 1;
            }

            @Override
            public @NotNull FluidStack getFluidInTank(int tank) {
                return menu.getFluidStack();
            }

            @Override
            public int getTankCapacity(int tank) {
                return menu.getFluidCapacity();
            }

            @Override
            public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
                return true;
            }

            @Override
            public int fill(FluidStack resource, FluidAction action) {
                return 0;
            }

            @Override
            public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
                return FluidStack.EMPTY;
            }

            @Override
            public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
                return FluidStack.EMPTY;
            }
        };

        addRenderableOnly(new FluidTankWidget(x + 80, y + 20, FluidTankWidget.TankVariants.NORMAL, fluidHandler));
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
    }
}
